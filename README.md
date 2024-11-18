# BioDock-Fiji-Compatibility
This is a repository for a plugin for FIJI and an associated google colab notebook which helps convert RLE encoded .json outputs from Biodock into ROI's to be manipulated natively in FIJI

# Steps to use (No programming experience necessary < 5 minutes)
If you are coming from Biodock you will need to output the mask files in the .json format. The google colab notebook (https://colab.research.google.com/drive/1f6h3vwrYuXhPHNMHoQhOR8-7Yji0krDS?usp=sharing) will take one .json file and output the equivalent as a file containing ROI Polyons for use in FIJI & ImageJ (Tested this plugin JAR in v1.54f and 1.54g of ImageJ2). 

From there, you should take the JAR file linked here (https://drive.google.com/file/d/1ntyoNwA2qseDR15__MUUpWBGTx2vnyZ2/view?usp=sharing) and copy it into the plugins folder in FIJI. You might have to restart FIJI.

To use the plugin in FIJI go Under **Plugins > ROI Tools > Batch ROI Importer** and select the file you have just received from the colab notebook. The importer should result in many ROI's now in your FIJI. It is up to you to manage the association between these ROI and the images they correspond to (the images you've given to Biodock to segment). 

Note: I am not associated with Biodock in any way, and frankly you shouldn't just run JAR files you find on the internet. You can mvn to produce the JAR file yourself using what is in the repository. If you're having trouble you can email me a dot j dot chabowski at gmail d o t  com
