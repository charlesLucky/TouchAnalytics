function [clientScoreMatrix,impostorScoreMatrix]=prediction(classifierName,trainingDataSet,trainUserLabels,testDataSet,testUserLabels,saveFilePath,user)
%% classifierName=name of classifier. Can receive knn, svm or discriminant

numFeatures=length(trainingDataSet(1,:));
clientProportion=size(trainUserLabels,1)/sum(strcmp(trainUserLabels,'client'));
impostorProportion=size(trainUserLabels,1)/sum(strcmp(trainUserLabels,'impostor'));


if strcmp(classifierName,'knn')
  %training knn
  classifier = ClassificationKNN.fit(trainingDataSet,trainUserLabels,'NumNeighbors',5);
elseif strcmp(classifierName,'svm')
  
  %% Using builtin svm  function of Matlab
  weights=zeros(size(trainingDataSet,1),1);
  
  indexClient = cellfun(@(x) strcmp(x,'client'), trainUserLabels);
  weights(indexClient==1) = clientProportion;
  weights(indexClient==0) = impostorProportion;
  
  classifier = fitcsvm(trainingDataSet,trainUserLabels,'KernelFunction','rbf','Standardize',true,'ClassNames',{'impostor','client'},'KernelScale','auto','Weights',weights);
  classifier = fitSVMPosterior(classifier);
elseif strcmp(classifierName,'libsvm')
  %% Using libsvm function
  addpath('lib/libsvm');
  
  [trainUserLabels,testUserLabels]=discretizeUserLabels(trainUserLabels,testUserLabels,classifierName);
    
  
  %% Training
  clientProportion=num2str(sum(trainUserLabels==1)/size(trainUserLabels,1));
  impostorProportion=num2str(sum(trainUserLabels==-1)/size(trainUserLabels,1));


%[c,g]=grid(trainUserLabels, trainingDataSet);
%c=num2str(c);
%g=num2str(g);
%classifier = svmtrain(trainUserLabels,trainingDataSet,['-h 0 -c ', c, ' -g ', g,' -b 1 -w-1 ',impostorProportion,' -w1 ',clientProportion]);
classifier = svmtrain(trainUserLabels,trainingDataSet,['-h 0 -b 1']);  
elseif strcmp(classifierName,'discriminant')
  classifier=fitcdiscr(trainingDataSet,trainUserLabels);
  
elseif strcmp(classifierName,'regression')
     
  [trainUserLabels,testUserLabels]=discretizeUserLabels(trainUserLabels,testUserLabels,classifierName);
  %creating the vector of weights
 % w_vector(trainUserLabels==1)=clientProportion;
  %w_vector(trainUserLabels==0)=impostorProportion;
    %Training
    classifier = glmfit(trainingDataSet,trainUserLabels,'binomial','link','logit');% 'weights', w_vector); 
    
end

%save classifier
save(strcat(saveFilePath,'/Classifier_User_',num2str(user),'.mat'),'classifier');

%taking client and impostor score matrix
[clientScoreMatrix,impostorScoreMatrix]=calculateScoreMatrix(classifier,testDataSet,testUserLabels,classifierName);
end