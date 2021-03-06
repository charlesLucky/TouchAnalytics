addpath ..
addpath C:\Users\Poh\Dropbox\LivDet\program\lib\VR-EER

%% load the data
clear
load('scrolling data.mat');

scrolling=cleaningdataset(scrolling);
zero_ = find(sum(scrolling)==0);
scrolling(:,zero_)=[];

% check the numbers
for i=1:size(scrolling,2),
  unique_count(i) = numel(unique(scrolling(:,i)));
end;

bar(unique_count);
ylabel('Unique values');
xlabel('Feature index');
%print('-dpng','Pictures/main_norman__unique_value_feature_count.png');

% normalise
selected_ = find(unique_count>50)
ID=scrolling(:,1);
data=(scrolling(:,selected_));
%data=zscore(scrolling(:,[2:end]));
clear scrolling

ID_list = unique(ID)'
%% 
load('BioHashingKey.mat','key');

dim = size(data,2);

data = biohashing_norman(data,key(1:dim, 1:dim));
imagesc(data);

data=double(data);

%%
c = cvpartition(ID,'KFold',3);

%% analyse the test folds (should be 1/3)
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

%% train classifiers in the biohashing domain 
% (EXACTLY the same code as main_norman.m)
for i=1:numel(ID_list),

  %positive training samples
  index_template = selected_user{TRAIN}{i}; %use all the available samples for training

  %negative training samples
  userlist = find(ID_list ~= i);
  userlist = userlist(TRAIN_IMP);
  index_template_neg = cell2mat(cellfun(@(x) x(1:10), selected_user{TRAIN}( userlist ), 'UniformOutput', false));  

  %logistic regression
  Y = [ones(1, numel(index_template)) zeros(1, numel(index_template_neg))];
  W = [ones(1, numel(index_template)) / numel(index_template) ones(1, numel(index_template_neg)) /numel(index_template_neg) ];
  com.user.b(i,:) = glmfit(data([index_template index_template_neg],:),Y', 'binomial', 'weights',W');

  %k-NN
  com.knn.mdl{i} = fitcknn(data([index_template index_template_neg],:),Y');
end;
bar(median(com.user.b))
com.median.b = median(com.user.b);

%% Compare the 4 methods
% (EXACTLY the same code as main_norman.m)
clear score*;
for k=1:2,
  for m=1:4,
    scores{k,m}=[];
  end;
end;

for i=1:numel(ID_list),

  %positive training samples
  index_template = selected_user{TRAIN}{i}; %use all the available samples for training

  %negative training samples
  userlist = find(ID_list ~= i);
  userlist = userlist(TRAIN_IMP);
  index_template_neg = cell2mat(cellfun(@(x) x(1:10), selected_user{TRAIN}( userlist ), 'UniformOutput', false));  
  
  %METHOD 1: Euclidean distance
  m=1;
  %genuine scores
  %score_gen = get_pairwise_triu_scores(data, selected_user{1}{i}); 
  %index_template = selected_user{TRAIN}{i}(1:min_train_samples);
  score_gen_ = get_pairwise_scores(data, index_template, selected_user{VALID}{i}); 
  score_gen{m} = - mean(score_gen_)';
    
  %impostor scores -- select only 10 samples
  userlist = find(ID_list ~= i);
  userlist = userlist(VALID_IMP);
  index_imp = cell2mat(cellfun(@(x) x(1:10), selected_user{VALID}( userlist ), 'UniformOutput', false));
  
  score_imp_ = get_pairwise_scores(data, index_template, index_imp);
  score_imp{m} = - mean(score_imp_)';
  
  %METHOD 2: logistic regression
  m=2;
  score_gen{m}= glmval(com.user.b(i,:)', data(selected_user{VALID}{i},:),'identity');
  score_imp{m} = glmval(com.user.b(i,:)', data(index_imp,:),'identity');
 
  %METHOD 3: logistic regression
  m=3;
  score_gen{m} = glmval(com.median.b', data(selected_user{VALID}{i},:),'identity');
  score_imp{m} = glmval(com.median.b', data(index_imp,:),'identity');
 
  %METHOD 4: K-NN
  m=4;
  com.knn.mdl{i}.NumNeighbors = 8;%4
  [~, gen_] = predict( com.knn.mdl{i}, data(selected_user{VALID}{i},:));
  [~, imp_] = predict( com.knn.mdl{i}, data(index_imp,:));
  score_gen{m}=gen_(:,2);
  score_imp{m}=imp_(:,2);
  
  %record down the scores
  for m=1:4,
    scores{1,m} = [scores{1,m}; score_imp{m}];
    scores{2,m} = [scores{2,m}; score_gen{m}];
  end;
  
  for m=1:4,
    eer_(i,m) = wer(scores{1,m}, scores{2,m});
    %eer_(i,m) = wer(score_imp{m}, score_gen{m}, [],2,[],m);
  end;
  %pause;
  fprintf(1,'.');
end;
fprintf(1,'\n');
save main_norman_biohash.mat scores;
%%
for m=1:4,
  eer_system(m) = wer(scores{1,m}, scores{2,m}, [],2,[],m);
end;
eer_system
legend('Euclidean','LR user-specific','LR common', 'kNN (8)','location', 'Southwest');

print('-dpng','Pictures/main_norman_biohash__DET_Euc_LR_kNN.png');
%% compare with main_norman
bline = load('main_norman.mat');

%%
m=4;
wer(bline.scores{1,m}, bline.scores{2,m}, [],2,[],1);
wer(scores{1,m}, scores{2,m}, [],2,[],2);
legend('baseline','biohash');
print('-dpng','Pictures/main_norman_biohash__DET_kNN_bline_vs_biohash.png');

