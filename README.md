# Photor
图片选择器，支持二级选择，支持相机拍照，自带裁剪功能</br>
内部引用框架：[Glide](https://github.com/bumptech/glide)、[EventBox](https://github.com/bigdongdong/EventBox)、[ClipView](https://github.com/bigdongdong/ClipView)

# demo下载
右击 -> 链接另存为</br>
[photor.apk](https://github.com/bigdongdong/Photor/blob/master/preview/photor.apk)</br></br>
<img  width = "300" src = "https://github.com/bigdongdong/Photor/blob/master/preview/demo_1.jpg"></img>

# 截图预览
## 图片选择（文件夹、相册、预览）
<img  width = "300" src = "https://github.com/bigdongdong/Photor/blob/master/preview/select_directory.jpg"></img>
<img  width = "300" src = "https://github.com/bigdongdong/Photor/blob/master/preview/select_album_2.jpg"></img>
<img  width = "300" src = "https://github.com/bigdongdong/Photor/blob/master/preview/pre.jpg"></img></br>

## 裁剪
#### 裁剪框架相关：[ClipView](https://github.com/bigdongdong/ClipView)</br></br>
<img  width = "300" src = "https://github.com/bigdongdong/Photor/blob/master/preview/clip.jpg"></img></br>

# 项目配置

```
  allprojects {
      repositories {
          ...
          maven { url 'https://jitpack.io' }  //添加jitpack仓库
      }
  }
  
  dependencies {
	  implementation 'com.github.bigdongdong:Photor:1.5' //添加依赖
  }
```

# 使用说明
```java
	//设置选择回调
	OnPhotorListener onPhotorListener = new OnPhotorListener() {
            @Override
            public void onSuccess(List<ImgBean> IMGs, EMSource source) {
                //IMGs 内包含 图片的url,名称,后缀名type,尺寸大小等信息
		//source为选择源，分为CAMERA拍照和ALBUM相册
            }
        };
	
	//从相机拍照
        Photor.getInstance()
                .context(this)
                .crop(400,400)              //裁剪参数，即最终返回的图片的宽高 [不写这行则不裁剪]
                .onPhotorListener(onPhotorListener)
                .requestImgFromCamera();
		
	//从文件夹选择
        Photor.getInstance()
                .context(this)
                .crop(400,400)              //裁剪参数，即最终返回的图片的宽高 [不写这行则不裁剪]
                .onPhotorListener(onPhotorListener)
                .requestImgsFromDirectory(9);       //从文件夹选择，最多选择9张


	//从相册选择
        Photor.getInstance()
                .context(this)
                .crop(400,400)              //裁剪参数，即最终返回的图片的宽高 [不写这行则不裁剪]
                .onPhotorListener(onPhotorListener)
                .requestImgsFromAlbum(9);       //从相册选择，最多选择9张
		
		
	//清除Photor缓存，缓存有两部分组成：裁剪后的图片，以及拍照的原图
	Photor.getInstance()
                        .context(this)
                        .clearCache();

```
