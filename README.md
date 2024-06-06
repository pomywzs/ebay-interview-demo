# ebay-interview-demo

### 说明

* 用户数据存储文件是项目根目录的 UserResourceSaving.json 文件，每行一个数据

* 添加用户资源 http://localhost:8080/admin/addUser
```
{
    "userId": 123456,
    "endpoint": [
        "resource A",
        "resource B",
        "resource C"
    ]
}
```
  文件中已添加好该资源
* 查询用户是否有资源权限 http://localhost:8080/user/resourceF

