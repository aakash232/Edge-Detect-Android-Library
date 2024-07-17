# Edge Detection Android Library (.AAR)

### **Edge detection** is an **image processing** technique for finding the _boundaries of objects within images_ using **Canny-edge detection algorithm** and **OpenCV**. 

> A technique used in a variety of real-life applications like medical imaging, finger-print scanning, and snappy filters on social media.

<br>
<img src="https://docs.opencv.org/4.x/canny1.jpg">


## How to use
#### Step 1. Import Module
Go to `File` -> `New` -> `Import Module`
   
#### Step 2. Adding Dependency
a) Go to `File` -> `Project Structure` -> `Dependencies`  

b) Select `app module` -> click `+` on top-left corner -> select `module dependency`

c) Choose `OpenCV` and rename `app` to `EdgeDetect`  
  
#### Step 3 
Instantiate object for EdgeDetectLibrary class and access the methods accordingly.
  
## Documentation
* [Implementing Library in Android](https://developer.android.com/studio/projects/android-library#psd-add-dependencies) 

## Methods
* Library initialization
    > edgeLib_init    
* Permission Manager
    > edgeLib_askPermission    
* Canny-edge detection algorithm
    > edgeLib_detectEdges    
* Store images in SQLite Database
    > edgeLib_storeImage    
* Image chooser for camera and gallery
    > edgeLib_imagePicker
* Upload images via URL
    > edgeLib_getBitmapByURL
