# TSProcessor
 
 This Time-Series Processor is part of my Bachelors Thesis on
 **Hierarchisches Clustering von Kinect-Bewegungsdaten mittels Dynamic Time Warping**
 (Hierarchical Clustering of Kinect-Motion-Data using Dynamic Time Warping).
 It can be used to cluster Time-Series Data (e.g. Kinect Data).
 
 ## Usage
 The whole project is written in plain Java.
 So you just need to install Java.
 The project has two start parameters.
 - The path of the config-file (mandatory).
 - A prefix for the paths in the config-file, like "D:" (optional).
 
 ## Configuration File
 The config-File contains all the information necessary to cluster the data.
 Make sure to avoid the colon sign **":"** in this file.
 Use the second parameter for the prefix instead.
 The following attributes have to be specified:
 - _inputPath_: The location of the dataset.
 - _outputPath_: The location you want to save the results to.
 - _separator_: The separator used in the dataset to separate values. e.g. "#".
 - _datasetType_: The type of dataset. e.g. "kinect".
 - _threshold_: The threshold for clustering. If the cost is greater than this threshold the clustering will be terminated.
 - _attributes_: The list of attributes this dataset offers.
 - _usedAttributes_: The subset of attributes you want to consider for the cost calculation.
 - _flipVisualization_: This attribute can be used to flip the visualization if the x-values are mirror inverted in this dataset.
 - _attributeForBodyIdentification_: The name of the parameter this dataset uses for the identification of different bodies.
 - _distanceFunction_: The name of the distance function you want to use (You can implement new ones).
 - _skipFrames_: Whether if every third frame should be ignored to increase performance.
 
 An example can be seen in: [Example Config-File](resources/app.config).
 
 ## Feedback
 If you have any questions or feedback, please reach out to me at laurenz.fuchs@unibw.de.
