% program is very similar to main_norman_biohash

addpath ..
addpath C:\Users\Poh\Dropbox\LivDet\program\lib\VR-EER
clear
%% load the data
%orientation='Scrolling';
orientation='Horizontal';

if strcmp(orientation,'Scrolling')
    load('scrolling data.mat');
    data=scrolling;
    clear scrolling;
else
    load('horizontal data.mat');
    data=horizontal;
    clear horizontal;
end

scenario={'homo','hete'};

data=cleaningdataset(data);
zero_ = find(sum(data)==0);
data(:,zero_)=[];

% check the numbers
for i=1:size(data,2),
  unique_count(i) = numel(unique(data(:,i)));
end;

% bar(unique_count);
%ylabel('Unique values');
%xlabel('Feature index');
%print('-dpng','Pictures/main_norman__unique_value_feature_count.png');

% normalise
selected_ = find(unique_count>50);
ID=data(:,1);
data=(data(:,selected_));

ID_list = unique(ID)';

%% analyse the test folds (should be 1/3)
if strcmp(orientation,'Horizontal')
    if exist('c_horizontal.mat', 'file'),
        load c_horizontal.mat
    else
        c = cvpartition(ID,'KFold',3);
        save c_horizontal.mat c
    end;
else
    if exist('c_scrolling.mat', 'file'),
        load c_scrolling.mat
    else
        c = cvpartition(ID,'KFold',3);
        save c_scrolling.mat c
    end;
end

clear selected_user;
for p=1:3,
  selected_user{p}=cell(1,41);
  for i=1:numel(ID_list),
    selected_user{p}{i} = find(c.test(p) &   ID==ID_list(i))';
  end;
end;

%%
TRAIN=1;
VALID=2;
TEST=3;
TRAIN_IMP=1:20; %impostor used for training
VALID_IMP=21:40;%impostor used for validation
TEST_IMP =21:40;%impostor used for test

%% load the common key
load('BioHashingKey.mat','key');

keySize=40;
dim = round(size(data,2)*keySize);

key = key(1:dim, 1:dim);
classifiers={'x','Logistic Regression per User','One Logistic Regression','kNN','SVM'};

scenario={'homo','hete'};
%% train classifiers in the bioconvolving domain 
for s=1:2
for i=1:numel(ID_list),

  %positive training samples
  index_template = selected_user{TRAIN}{i}; %use all the available samples for training

  %negative training samples
  userlist = find(ID_list ~= i);
  userlist = userlist(TRAIN_IMP);
  index_template_neg = cell2mat(cellfun(@(x) x(1:10), selected_user{TRAIN}( userlist ), 'UniformOutput', false));  

  %for each user, the template is encrypted using one common key; and the attacker
  %uses another key for nonmatch
  if strcmp(scenario{s},'homo')
    X_gen =double(biohashing(data(index_template,:),key));
    X_imp = double(biohashing(data(index_template_neg,:),key));
  else
   com.user.key{i} = rand(dim);
    X_gen =double(biohashing(data(index_template,:),com.user.key{i}));
    X_imp=[];
    for iUser=1:numel(userlist)
      index_template_neg = cell2mat(cellfun(@(x) x(1:10), selected_user{TRAIN}( iUser ), 'UniformOutput', false));
      
      key_imp =rand(dim);
      % Encode the impostor user, encode its data with a key
      p_data=double(biohashing(data(index_template_neg,:),key_imp));
      X_imp = [X_imp;p_data];
    end
  end

  %X_gen = double(biohashing(data(index_template,:),key));
  %X_imp = double(biohashing(data(index_template_neg,:),key));
  
  index_template_neg = cell2mat(cellfun(@(x) x(1:10), selected_user{TRAIN}( userlist ), 'UniformOutput', false)); 
  %logistic regression
  Y = [ones(1, numel(index_template)) zeros(1, numel(index_template_neg))];
  W = [ones(1, numel(index_template)) / numel(index_template) ones(1, numel(index_template_neg)) /numel(index_template_neg) ];
%   com.user.b(i,:) = glmfit([X_gen; X_imp],Y', 'binomial', 'weights',W');

  %k-NN
  com.knn.mdl{i} = fitcknn([X_gen; X_imp],Y');

 %SVM
  com.svm{i}=fitcsvm([X_gen;X_imp],Y','KernelFunction','rbf','Standardize',true,'KernelScale','auto');
  %com.svm{i} = fitSVMPosterior(com.svm{i});
end;
% bar(median(com.user.b))
% com.median.b = median(com.user.b);

%% Compare the 4 methods
% (SIMILAR to main_norman.m)
clear score*;
for k=1:2,
  for m=1:5,
    scores{k,m}=[];
  end;
end;

%% Testing
for i=1:numel(ID_list),

  %positive training samples
  index_gen = selected_user{VALID}{i}; %use all the available samples for training
  
  %impostor scores -- select only 10 samples from the VALIDATION set
  userlist = find(ID_list ~= i);
  userlist = userlist(VALID_IMP);
  index_imp = cell2mat(cellfun(@(x) x(1:10), selected_user{VALID}( userlist ), 'UniformOutput', false));
  
  %for each user, the template is encrypted using one common key; and the attacker
  %   uses another key for nonmatch
  
  if strcmp(scenario{s},'homo')
    X_gen = double(biohashing(data(index_gen,:),key));
    X_imp = double(biohashing(data(index_imp,:),key));
  else
    X_gen = double(biohashing(data(index_gen,:),com.user.key{i}));
    X_imp = double(biohashing(data(index_imp,:),com.user.key{i}));
  end
%   X_gen = double(biohashing(data(index_gen,:),key));
%   X_imp = double(biohashing(data(index_imp,:),key));
  
  
%   METHOD 2: logistic regression
%   m=2;
%   score_gen{m} = glmval(com.user.b(i,:)', X_gen,'identity');
%   score_imp{m} = glmval(com.user.b(i,:)', X_imp,'identity');
%  
%   METHOD 3: logistic regression
%   m=3;
%   score_gen{m} = glmval(com.median.b', X_gen,'identity');
%   score_imp{m} = glmval(com.median.b', X_imp,'identity');
 
  %METHOD 4: K-NN
  m=4;
  com.knn.mdl{i}.NumNeighbors = 8;%4
  [~, gen_] = predict( com.knn.mdl{i}, X_gen);
  [~, imp_] = predict( com.knn.mdl{i}, X_imp);
  score_gen{m}=gen_(:,2);
  score_imp{m}=imp_(:,2);

 %METHOD 5: SVM
  m=5;
  [~,gen_] =predict(com.svm{i},X_gen);
  [~,imp_] =predict(com.svm{i},X_imp);
  score_gen{m}=gen_(:,2);
  score_imp{m}=imp_(:,2);
  
  
  %record down the scores
  for m=4:5,,
    scores{1,m} = [scores{1,m}; score_imp{m}];
    scores{2,m} = [scores{2,m}; score_gen{m}];
  end;
  
  for m=4:5,,
    eer_(i,m) = wer(scores{1,m}, scores{2,m});
    %eer_(i,m) = wer(score_imp{m,keySize}, score_gen{m,keySize}, [],2,[],m);
  end;
  %pause;
  fprintf(1,'.');
end;
fprintf(1,'\n');
extension='.mat';
fileName=['main_norman_biohash_',scenario{s},'_known-',orientation,'-kSize-',num2str(keySize)];
    save([fileName,extension],'scores');
end

%%
figure(2);
for m=4:5,,
  eer_system(m) = wer(scores{1,m}, scores{2,m}, [],2,[],m);
end;
eer_system
legend('LR user-specific','LR common', 'kNN (8)','location', 'Southwest');

for i=1:2
    fileName=['main_norman_biohash_',scenario{i},'_known'];
    file=['Pictures/',fileName,'__DET_Euc_LR_kNN.png'];
    print('-dpng',file);
end

%% compare with main_norman
for i=1:2  
  bline = load(['main_norman-',orientation,'.mat']);
  for keySize=[1]%,50,75,100,200,400]
    bhash = load(['main_norman_biohash_',scenario{i},'_Unknown-',orientation,'-kSize-',num2str(keySize)]);    
    figure(3);
    m=5;
    wer(bline.scores{1,m}, bline.scores{2,m}, [],2,[],1);
    wer(bhash.scores{1,m}, bhash.scores{2,m}, [],2,[],2);
    wer(scores{1,m}, scores{2,m}, [],2,[],3);
    legend('Baseline','BioHashing Unknown','BioHashing Known');
    title({['DET - Classifier: ',classifiers{m},' using DubleSum-',orientation]});    
    file=['Pictures/DET_Comparative/DET_',classifiers{m},'_bline_vs_biohash-',orientation,'-',scenario{i},'_known.png'];
    print('-dpng',file);
  end
end
%%
figure(4);
wer(bhash.scores{1,m}, bhash.scores{2,m}, [],4,[],1);
wer(scores{1,m}, scores{2,m}, [],4,[],2);

%%
bhash_known_hete1 = load(['main_norman_biohash_hete_known-',orientation,'-kSize-1.mat']);
bhash_known_hete2 = load(['main_norman_biohash_hete_known-',orientation,'-kSize-2.mat']);
bhash_known_hete3 = load(['main_norman_biohash_hete_known-',orientation,'-kSize-3.mat']);
bhash_known_hete4 = load(['main_norman_biohash_hete_known-',orientation,'-kSize-4.mat']);
bhash_known_hete8 = load(['main_norman_biohash_hete_known-',orientation,'-kSize-8.mat']);
bhash_known_hete40 = load(['main_norman_biohash_hete_known-',orientation,'-kSize-40.mat']);
figure(5)
wer(bline.scores{1,m}, bline.scores{2,m}, [],2,[],1);
wer(bhash_known_hete1.scores{1,m}, bhash_known_hete1.scores{2,m}, [],2,[],2);
wer(bhash_known_hete2.scores{1,m}, bhash_known_hete2.scores{2,m}, [],2,[],3);
wer(bhash_known_hete3.scores{1,m}, bhash_known_hete3.scores{2,m}, [],2,[],4);
wer(bhash_known_hete4.scores{1,m}, bhash_known_hete4.scores{2,m}, [],2,[],5);
wer(bhash_known_hete8.scores{1,m}, bhash_known_hete8.scores{2,m}, [],2,[],6);
wer(bhash_known_hete40.scores{1,m}, bhash_known_hete40.scores{2,m}, [],2,[],7);
legend('baseline','biohash known-kSize=25','biohash known-kSize=50','biohash known-kSize=75','biohash known-kSize=100','biohash known-kSize=200','biohash known-kSize=1000');
title(['DET Comparison KeySize - Biohashing - Know Scenario-',orientation])
file=['Pictures/DET_Comparative/KeySize-DET_kNN_bline_vs_biohash-',orientation,'-known.png'];
print('-dpng',file);