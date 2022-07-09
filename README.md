# rzk-videos

> 微信小程序后台api接口

# 部署流程

## maven打包后

### 把jar包和Dockerfile文件上传服务器放在同一个文件中

> docker build -t rzk-api .
> docker run -d --restart=always --name rzk-api -v /usr/local/logs:/home/jar-logs -p 8090:80 rzk-api


## maven分离lib部署

> 指定环境 -Dspring.profiles.active=test
> 后台运行 nohup
> 重定向 > /root/null 2>&1 &

```
nohup java -jar rzk-videos-api-1.0-SNAPSHOT.jar  --server.port=8090  -Dspring .config.additional-location=/config/application.yml  --spring.profiles.active=dev  > /root/null 2>&1 &
```

> 上面命令的解释：
（1）、nohup 表示永久运行。
（2）、& 表示后台运行 。
（3）、> 代表重定向到哪里。
（4）、1 表示stdout标准输出，系统默认值是1。所以">/dev/null"等同于"1>/dev/null"
（5）、2 表示stderr标准错误，nohup ./mqnamesrv >/home/mqnamesrv.out 2>&1 & 表示标准输出到mqnamesrv.out中，接着，标准错误输出重定向等同于标准输出，输出到同一文件中。


> 打包分离

```aidl
 <build>
        <plugins>
            <!-- 打JAR包，不包含依赖文件；显式剔除配置文件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <!--${env.LEARN_HOME}为项目配置的环境变量，下同-->
                    <outputDirectory>${project.build.directory}/${output.jar.file.path}</outputDirectory>
                    <!-- 将配置文件排除在jar包 -->
                    <excludes>
                        <exclude>*.properties</exclude>
                        <exclude>*.yml</exclude>
                        <exclude>*.xml</exclude>
                        <exclude>*.txt</exclude>
                    </excludes>
                    <archive>
                        <!-- 生成的jar中，包含pom.xml和pom.properties这两个文件 -->
                        <addMavenDescriptor>true</addMavenDescriptor>
                        <!-- 生成MANIFEST.MF的设置 -->
                        <manifest>
                            <!--这个属性特别关键，如果没有这个属性，有时候我们引用的包maven库 下面可能会有多个包，并且只有一个是正确的，
                            其余的可能是带时间戳的，此时会在classpath下面把那个带时间戳的给添加上去，然后我们 在依赖打包的时候，
                            打的是正确的，所以两头会对不上，报错。 -->
                            <useUniqueVersions>false</useUniqueVersions>
                            <!-- 为依赖包添加路径, 这些路径会写在MANIFEST文件的Class-Path下 -->
                            <addClasspath>true</addClasspath>
                            <!-- MANIFEST.MF 中 Class-Path 各个依赖加入前缀 -->
                            <!--这个jar所依赖的jar包添加classPath的时候的前缀，需要 下面maven-dependency-plugin插件补充-->
                            <!--一定要找对目录，否则jar找不到依赖lib，前边加../是因为jar在bin下，而bin与lib是平级目录-->
                            <classpathPrefix>../${output.dependence.file.path}</classpathPrefix>
                            <!--指定jar启动入口类 -->
                            <mainClass>com.rzk.Application</mainClass>
                        </manifest>
                        <manifestEntries>
                            <!-- 假如这个项目可能要引入一些外部资源，但是你打包的时候并不想把 这些资源文件打进包里面，这个时候你必须在
                            这边额外指定一些这些资源文件的路径,假如你的pom文件里面配置了 <scope>system</scope>,就是你依赖是你本地的
                            资源，这个时候使用这个插件，classPath里面是不会添加，所以你得手动把这个依赖添加进这个地方 -->
                            <!--MANIFEST.MF 中 Class-Path 加入自定义路径，多个路径用空格隔开 -->
                            <!--此处resources文件夹的内容，需要maven-resources-plugin插件补充上-->
                            <Class-Path>../${output.resource.file.path}</Class-Path>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>

            <!-- 复制依赖的jar包到指定的文件夹里 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <!-- 拷贝项目依赖包到指定目录下 -->
                            <outputDirectory>${project.build.directory}/${output.dependence.file.path}</outputDirectory>
                            <!-- 是否排除间接依赖，间接依赖也要拷贝 -->
                            <excludeTransitive>false</excludeTransitive>
                            <!-- 是否带上版本号 -->
                            <stripVersion>false</stripVersion>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <!-- 用于复制指定的文件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <executions>
                    <!-- 复制配置文件 -->
                    <execution>
                        <id>copy-resources</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <resources>
                                <resource>
                                    <directory>src/main/resources</directory>
                                    <includes>
                                        <!--将如下格式配置文件拷贝-->
                                        <exclude>*.properties</exclude>
                                        <exclude>*.yml</exclude>
                                        <exclude>*.xml</exclude>
                                        <exclude>*.txt</exclude>
                                    </includes>
                                </resource>
                            </resources>
                            <!--输出路径-->
                            <outputDirectory>${project.build.directory}/${output.resource.file.path}</outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

