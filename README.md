# H2O 3 and AWS SageMaker Integration

Proof-of-Concept for integrating H2O-3 AutoML with Amazon SageMaker

#### Content of Repo, See below for explanation of files:
- README.md
- Dockerfile
- hyperparameters.json
- sample_sagemaker_notebook.ipynb
- automl_scripts
  - train
  - serve
  - inference-server (this folder contains files for building inference server docker application)

#### Dockerfile

Used to build the docker image that AWS SageMaker will use for model training purposes

#### hyperparameters.json

Editable. Contains three nested dictionaries which will be ingested and used during training
1. "training" --> will be used to pass along settings such as whether or not to train as a classification problem
2. "h2o" --> dictionary of all keyword arguments for [h2o.init()](http://docs.h2o.ai/h2o/latest-stable/h2o-docs/starting-h2o.html)
3. "aml" --> dictionary of all keyword arguments for [H2OAutoML()](http://docs.h2o.ai/h2o/latest-stable/h2o-docs/automl.html#required-parameters)

#### sample_sagemaker_notebook

Example of what a jupyter notebook might look like within the AWS SageMaker notebook instance

#### automl_scripts

The backend code that tells AWS SageMaker what it is expected to do.

files:
- inference server folder is used to build the inference server docker application.
  predict() method inside InferenceServer.scala file in this folder is currently hard coded to predict the results using MOJO model.
- train  will train the model and download the MOJO model. The hyperparameters in it are currently hard coded.

# To Deploy:
1. Create an S3 Bucket with "sagemaker" somewhere in the name. Upload the training data here.

2. The Inference Server Docker application uses a light-weight web micro-framework called 'Scalatra' to serve the http application. To build and deploy the Inference Server docker application: (please install scala, sbt and docker on your local machine)

$> cd inference-server
$> sbt assembly
$> sh build_and_sh.sh inference-server
   (the above command should be run after entering the main folder)

3. Create a sagemaker notebook instance and upload the sample_sagemaker_notebook.ipynb. S3 bucket path and AWS ECS repository name needs to changed in this file. Run the notebook.
4. Rest API will be created. The results obtained are based on hard coded values in predict() method inside InferenceServer.scala file.
