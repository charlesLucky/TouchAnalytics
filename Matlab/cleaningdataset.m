function [dataset]=cleaningdataset(dataset)
% This function remove Inf and NaN samples
dataset(isnan(dataset)) = 0 ;
dataset(isinf(dataset)) = 0 ;

%removing phoneId,docId
dataset(:,[2,13])=[];

end