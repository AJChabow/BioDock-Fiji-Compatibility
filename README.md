# BioDock-Fiji-Compatibility
This is a repository for a plugin for FIJI and an associated google colab notebook which helps convert RLE encoded .json outputs from Biodock into ROI's to be manipulated natively in FIJI

# Steps to use (No programming experience necessary < 5 minutes)
If you are coming from Biodock you will need to output the mask files in the .json format. The google colab notebook (https://colab.research.google.com/drive/1f6h3vwrYuXhPHNMHoQhOR8-7Yji0krDS?usp=sharing) will take one .json file and output the equivalent as a file containing ROI Polyons for use in FIJI & ImageJ (Tested this plugin JAR in v1.54f and 1.54g of ImageJ2). 

From there, you should take the JAR file linked here (https://drive.google.com/file/d/1ntyoNwA2qseDR15__MUUpWBGTx2vnyZ2/view?usp=sharing) and copy it into the plugins folder in FIJI. You might have to restart FIJI.

To use the plugin in FIJI go Under **Plugins > ROI Tools > Batch ROI Importer** and select the file you have just received from the colab notebook. The importer will import the polygons into your ROI manager in FIJI. You can then load the original image you used for segmenting in Biodock and see your polygons laid over it.

Note: I am not associated with Biodock in any way, and frankly you shouldn't just run JAR files you find on the internet. You can use maven to produce the JAR file yourself using what is in the repository. If you're having trouble you can email me AT    a dot j dot chabowski at gmail d o t  com   (written this way because of robots)
