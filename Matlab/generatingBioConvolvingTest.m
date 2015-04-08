function [bioC_test]=generatingBioConvolvingTest(testSet,user,saveFilePath,optionkey,keySize)
% testSet= test dataset used in data protection
% user= user label of testSet
% saveFilePath= Full Path where the biohashing test set will be saved
% optionkey =
% 1: use the same key to all the users
% 2: use a different key to each user
% keySize= size of key. maximum is 1

bioC_test=[];

%% Same key for all users
if optionkey==1
    nFeatures=length(testSet(1,2:end));
    key=[0,round(nFeatures/2),nFeatures];
    bioC_test=biohashing(testSet(:,2:end),key);
elseif optionkey==2
    %% Different Keys for each user
    numFeatures=length(testSet(1,2:end));
    users=unique(testSet(:,1));
    for currentUser=1:length(users)
        % user data presented in testSet
        userData=testSet(find(testSet(:,1) == users(currentUser)),:);
        
        
        % creating the key for the currentUser
        % 2 is the standard size of bioconvolving
        % TODO: In future analysis the performance changing the size of key
        key=generateBioConvolvingKey(2,numFeatures);
         %This is was implemented because some times the key has this
        %behavior [0,31,31]. Thus, the protected data is the same the
        %original one. So, I decided to cut the sample to fit the other
        %protected one.
       for i=1:length(key)-1
        if key(i+1)-key(i)==numFeatures
            key=generateBioConvolvingKey(2,numFeatures);
            break;
        end
        end
        
        % Protecting the User data
        bioConvolvingData=bioconvolving(userData(:,2:end),key);
        
        % Adding the biohashingData to the Protected BioHashing test
        % dataset
        bioC_test=[bioC_test; bioConvolvingData];
    end
end

% Adding the user label to the biohashing data
bioC_test=[bioC_test testSet(:,1)];

% Discretizing the user. 1, for user, and 0 for remaining users
[bioC_test,testUserLabels]=discretizeUser(str2num(user),length(bioC_test(1,:)),bioC_test);

%% Folder used to save the biohashing data
% If empty create the variable
if(isempty(saveFilePath))
    saveFilePath=strcat(pwd(),'/Data/Horizontal/BioConvolving/Same_Key/User_',user);
end
% If the folder doesn't exist create it
if ~exist(saveFilePath,'dir')
    mkdir(saveFilePath);
end

%% Saving the testing data
save(strcat(saveFilePath,'/testSet.mat'),'bioC_test','testUserLabels');

end