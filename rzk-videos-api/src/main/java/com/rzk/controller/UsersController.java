package com.rzk.controller;

import com.rzk.pojo.Users;
import com.rzk.pojo.UsersReport;
import com.rzk.pojo.vo.PublisherVideos;
import com.rzk.pojo.vo.UsersVo;
import com.rzk.service.UsersService;
import com.rzk.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


@RestController
@Api(value = "用户有关业务的接口",tags ={"用户业务controller"} )
@RequestMapping("/users")
public class UsersController extends BasicController {
	@Autowired
       private 	UsersService usersService;
	@ApiOperation(value = "用户头像上传",notes = "用户头像上传的接口")

	@ApiImplicitParam(name="userId",value = "用户的id",required =true,dataType = "String",paramType = "query")
	@PostMapping("/upLoadImage")
	public JSONResult upLoadImage(String userId,@RequestParam("file") MultipartFile[] image) throws Exception {
		if(StringUtils.isBlank(userId)){
			return JSONResult.errorMsg("用户id不能为空");
		}
		//图片上传路径
		String fileDownloadPath = "C:\\lnsf_mod_dev";
		//String fileDownloadPath = "/opt/lnsf_mod_dev";
		//图片保存路径
		String fileUploadPath ="/"+userId+"/image";
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
		users.setFaceImage(fileUploadPath);
		usersService.updateUsersInfo(users);
		return JSONResult.ok(fileUploadPath);
	}
	@ApiOperation(value = "用户信息查询",notes = "用户信息查询的接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name="userId",value = "用户的id",required =true,dataType = "String",paramType = "query"),
			@ApiImplicitParam(name="fanId",value = "粉丝的id",required =true,dataType = "String",paramType = "query"),
	})

	@PostMapping("/queryUsers")
	public JSONResult queryUsers(String userId,String fanId)  {
		if(StringUtils.isBlank(userId)){
			return JSONResult.errorMsg("用户id不能为空");
		}
		Users users = usersService.queryUsersInfo(userId);
		UsersVo usersVo = new UsersVo();
		BeanUtils.copyProperties(users,usersVo);
		if (StringUtils.isNotBlank(fanId))
		usersVo.setFollow(	usersService.queryIfFollow(userId,fanId));
		return JSONResult.ok(usersVo);
	}



	@PostMapping("/queryPublisher")
	public JSONResult queryPublisher(String loginUserId,String videoId,String publishUserId)  {
		if(StringUtils.isBlank(publishUserId)){
			return JSONResult.errorMsg("");
		}
		Users users = usersService.queryUsersInfo(publishUserId);
		//查询视频发布者信息
		UsersVo usersVo = new UsersVo();
		BeanUtils.copyProperties(users,usersVo);
		//查询当前登录者的点赞关系
		boolean userLikeVideo = usersService.isUserLikeVideo(loginUserId,videoId);
		PublisherVideos bean = new PublisherVideos();
		bean.setPublisher(usersVo);
		bean.setUserLikeVideo(userLikeVideo);
		return JSONResult.ok(bean);
	}


	@PostMapping("/addYourFans")
	public JSONResult addYourFans(String userId,String fanId)  {
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
			return JSONResult.errorMsg("");
		}
		usersService.insertUserFanRelation(userId,fanId);
		return JSONResult.ok("关注用户成功");
	}
	@PostMapping("/delYourFans")
	public JSONResult delYourFans(String userId,String fanId)  {
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
			return JSONResult.errorMsg("");
		}
		usersService.deleteUserFanRelation(userId,fanId);
		return JSONResult.ok("取消用户关注");
	}

	@PostMapping("/reportUser")
	public  JSONResult reportUser(@RequestBody UsersReport usersReport){
		usersService.reportUser(usersReport);
		return JSONResult.errorMsg("举报核对中");
	}








}
