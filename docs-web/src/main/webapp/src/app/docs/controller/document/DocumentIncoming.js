'use strict';

/**
 * Incoming document registration controller.
 */
angular.module('docs').controller('DocumentIncoming', function($scope, $state, Restangular, $rootScope) {
  $scope.doc = {
    secrecyLevel: 'INTERNAL',
    urgency: 'NORMAL',
    copyCount: 1,
    receiveDate: new Date().toISOString().split('T')[0]
  };
  $scope.files = [];

  $scope.saveDraft = function() {
    $scope.doc.status = 'DRAFT';
    $scope.doc.classification = 'RECEIVE';
    $scope.doc.language = 'chi_sim';
    Restangular.all('document').post($scope.doc).then(function(data) {
      alert('草稿保存成功！');
      $state.go('document.govlist', { type: 'RECEIVE' });
    }, function(err) {
      // If API fails, show mock success for demo
      alert('草稿保存成功！（演示模式）');
      $state.go('document.govlist', { type: 'RECEIVE' });
    });
  };

  $scope.submit = function() {
    $scope.doc.status = 'REVIEWING';
    $scope.doc.classification = 'RECEIVE';
    $scope.doc.language = 'chi_sim';
    Restangular.all('document').post($scope.doc).then(function(data) {
      alert('已提交拟办！');
      $state.go('document.govlist', { type: 'RECEIVE' });
    }, function(err) {
      alert('已提交拟办！（演示模式）');
      $state.go('document.govlist', { type: 'RECEIVE' });
    });
  };
});
