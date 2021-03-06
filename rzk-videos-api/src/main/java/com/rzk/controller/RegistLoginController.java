package com.rzk.controller;

import com.rzk.pojo.IdentityUsers;
import com.rzk.pojo.Users;
import com.rzk.pojo.vo.UsersVo;
import com.rzk.service.IdentityUsersService;
import com.rzk.service.PunishUsersService;
import com.rzk.service.UsersService;
import com.rzk.utils.AvatarHelper;
import com.rzk.utils.JSONResult;
import com.rzk.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.ibatis.annotations.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@RestController
@Api(value = "用户注册登录的接口",tags ={"注册和登录的controller"} )
public class RegistLoginController extends BasicController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private IdentityUsersService identityUsersService;
	@Autowired
	private PunishUsersService punishUsersService;

	@ApiOperation(value = "用户注册", notes = "用户注册的接口")
	@PostMapping("/regist")
	public JSONResult regist(@RequestBody Users users) throws Exception {
		//判断是否是微信小程序注册
		if (users.getId()!=null){
			//2.判断用户名是否存在
			boolean userNameIsExist = usersService.queryUsernameIsExist(users.getUsername());

			//3.保存用户注册信息
			if (!userNameIsExist) {
				users.setNickname(users.getUsername());
				users.setPassword(null);
				users.setFaceImage(AvatarHelper.BASE64_PREFIX +AvatarHelper.createBase64Avatar(Math.abs("springboot.io".hashCode())));
				users.setFansCounts(0);
				users.setReceiveLikeCounts(0);
				users.setFollowCounts(0);
				users.setUserHidden(0);
				users.setUserStatus(0);
				usersService.SaveUsers(users);
			} else {
				return JSONResult.errorMsg("用户名已经存在");
			}
			users.setPassword("");
			UsersVo usersVO = setUsersRedisSessionKey(users);
			return JSONResult.ok(usersVO);
		}
		//1.判断用户名与密码不为空
		if (StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())) {
			return JSONResult.errorMsg("用户名密码不能为空");
		}
		//2.判断用户名是否存在
		boolean userNameIsExist = usersService.queryUsernameIsExist(users.getUsername());
		//3.保存用户注册信息
		if (!userNameIsExist) {
			users.setNickname(users.getUsername());
			users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
			users.setFaceImage(AvatarHelper.BASE64_PREFIX +AvatarHelper.createBase64Avatar(Math.abs("springboot.io".hashCode())));
			users.setFansCounts(0);
			users.setReceiveLikeCounts(0);
			users.setFollowCounts(0);
			users.setUserHidden(0);
			users.setUserStatus(0);
			usersService.SaveUsers(users);
		} else {
			return JSONResult.errorMsg("用户名已经存在");
		}
		users.setPassword("");
		UsersVo usersVO = setUsersRedisSessionKey(users);
		return JSONResult.ok(usersVO);
	}


	@PostMapping("/changePassword")
	public JSONResult changePassword(@RequestBody Users users) throws Exception {
		//1.判断密码不为空
		if (  StringUtils.isBlank(users.getPassword())) {
			return JSONResult.errorMsg("密码不能为空");
		}

		//2.更新用户注册信息
			users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
			usersService.ChangeUsersPassword(users);
		return JSONResult.ok();
	}

	public UsersVo setUsersRedisSessionKey(Users users) {
		String uniqueKey = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + users.getId(), uniqueKey, 1000 * 60 * 15);
		UsersVo usersVO = new UsersVo();
		BeanUtils.copyProperties(users, usersVO);
		usersVO.setUserKey(uniqueKey);
		return usersVO;
	}


	@ApiOperation(value = "用户登录", notes = "用户登录的接口")
	@PostMapping("/login")
	public JSONResult login(@RequestBody Users users) throws Exception {
		String username = users.getUsername();
		String password = users.getPassword();
		int   status= 0;
		//1.判断用户名与密码不为空
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return JSONResult.errorMsg("用户名密码不能为空");
		}
		//2.判断用户名是否存在
		Users usersResult = usersService.queryUsersForLogin(username, MD5Utils.getMD5Str(password));
		//3.返回
		if (usersResult != null) {
			//3.封禁时间是否到期
			status=punishUsersService.selectByDate(usersResult.getId());
			if (status==0){
				return JSONResult.errorMsg("用户封禁中~");
			}
			Users users1 = usersService.queryUsersForStatus(username);
			if (users1.getUserStatus() == 1) {
				usersResult.setPassword("");
				UsersVo usersVO = setUsersRedisSessionKey(usersResult);
				return JSONResult.ok(usersVO);
			} else if (users1.getUserStatus() == 0) {
				return JSONResult.errorMsg("用户审核中~~");
			} else{
				return JSONResult.errorMsg("用户封禁中~");
			}
		} else {
			return JSONResult.errorMsg("用户名或密码不正确");
		}
	}

	@PostMapping("/findIdentity")
	public JSONResult findIdentity(String username) throws Exception { ;
		//1.判断用户名与密码不为空
		if (StringUtils.isBlank(username)) {
			return JSONResult.errorMsg("用户名密码不能为空");
		}
		//2.判断用户名是否存在
		Users users= usersService.queryUserInfoByUsername(username);

		//3.返回
		if (users != null) {
			String userId =users.getId();
		IdentityUsers identityUsers =	identityUsersService.selectIdentityUsers(userId);
			if (identityUsers != null){
				return JSONResult.ok(identityUsers);
			}
			else {
				return JSONResult.errorMsg("用户密保查询不到");
			}
		} else {

			return JSONResult.errorMsg("查询不到用户");
		}
	}

	@ApiOperation(value = "用户注销",notes = "用户注销的接口")
	@ApiImplicitParam(name="userId",value = "用户的id",required =true,dataType = "String",paramType = "query")
	@PostMapping("/logout")
	public JSONResult logout( String userId) throws Exception {
		if (userId != null||userId != ""){
			redis.del(USER_REDIS_SESSION+":"+userId);
			 return JSONResult.ok();
		}else
			return JSONResult.errorMsg("用户名或密码不正确");
		}


	@PostMapping("/userIdentity")
	public JSONResult userIdentity( @RequestBody IdentityUsers identityUsers) throws Exception {
		if (identityUsers.getAnswer1().length()>0 && identityUsers.getAnswer2().length()>0 && identityUsers.getAnswer3().length()>0
		&& identityUsers.getQuestion1().length()>0 && identityUsers.getQuestion2().length()>0 && identityUsers.getQuestion3().length()>0
		&& identityUsers.getUserId().length()>0){
			identityUsersService.insertIdentityUsers(identityUsers);
			return JSONResult.ok();
		} else
			return JSONResult.errorMsg("密保填写有误！");
	}

	@PostMapping("/IdentityPassword")
	public JSONResult IdentityPassword( @RequestBody IdentityUsers identityUsers) throws Exception {
		if (identityUsers.getAnswer1().length()>0 && identityUsers.getAnswer2().length()>0 && identityUsers.getAnswer3().length()>0
				&& identityUsers.getQuestion1().length()>0 && identityUsers.getQuestion2().length()>0 && identityUsers.getQuestion3().length()>0
				&& identityUsers.getUserId().length()>0){
		IdentityUsers identityUsers1 =	identityUsersService.selectAnswerIdentityUsers(identityUsers);
			if (identityUsers1 != null){
				return JSONResult.ok();
			}else {
				return JSONResult.errorMsg("密保填写有误！");
			}
		} else
			return JSONResult.errorMsg("密保填写有误！");
	}

	@PostMapping("/uploadUserFaces")
	public JSONResult uploadUserFaces(String userId,@RequestParam("file") MultipartFile[] image) throws Exception {
		if(StringUtils.isBlank(userId)){
			return JSONResult.errorMsg("用户id不能为空");
		}
		//图片上传路径
		String fileDownloadPath = "C:\\lnsf_mod_dev";
			//	String fileDownloadPath = "/opt/lnsf_mod_dev";
		//图片保存路径
		String fileUploadPath ="/"+userId+"/userFaces";
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if(image !=null &&image.length>0){

				String imageName = image[0].getOriginalFilename();
				if(StringUtils.isNotBlank(imageName)){
					//图片上传最终路径
					String imageFinalLocal = fileDownloadPath + fileUploadPath + "/" + imageName;
					//图片最终	保存路径
					fileUploadPath += ("/" + imageName);
					File outImage = new File(imageFinalLocal);
					if(outImage.getParentFile()!=null ||!outImage.getParentFile().isDirectory()){
						//创建父文件
						outImage.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outImage);
					inputStream = image[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
			}else {
				return JSONResult.errorMsg("上传功能出错");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fileOutputStream !=null){
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		Users users = new Users();
		users.setId(userId);
		users.setUserFaces(fileUploadPath);
		usersService.updateUsersInfo(users);
		return JSONResult.ok(fileUploadPath);
	}
}
