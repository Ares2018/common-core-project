### 浙江新闻 - 核心模块

#### 添加依赖
> 1、 Project build.gradle  

```
allprojects {
    repositories {
        jcenter()
        maven { url "http://10.100.47.176:8086/nexus/content/groups/public" }
    }
}
```
> 2、 Module build.gradle  

```
compile 'cn.daily.android:core-library:0.0.6-SNAPSHOT'

```