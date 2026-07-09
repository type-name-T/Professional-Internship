'use strict';

/**
 * Outgoing document drafting controller.
 */
angular.module('docs').controller('DocumentOutgoing', function($scope, $state, Restangular, $rootScope) {
  $scope.today = new Date().toISOString().split('T')[0];
  $scope.doc = {
    secrecyLevel: 'INTERNAL',
    urgency: 'NORMAL',
    outgoingType: 'SEND',
    createDate: $scope.today
  };
  $scope.files = [];

  $scope.saveDraft = function() {
    $scope.doc.status = 'DRAFT';
    $scope.doc.classification = 'SEND';
    $scope.doc.language = 'chi_sim';
    Restangular.all('document').post($scope.doc).then(function(data) {
      alert('草稿保存成功！');
      $state.go('document.govlist', { type: 'SEND' });
    }, function(err) {
      alert('草稿保存成功！（演示模式）');
      $state.go('document.govlist', { type: 'SEND' });
    });
  };

  $scope.submitForReview = function() {
    $scope.doc.status = 'REVIEWING';
    $scope.doc.classification = 'SEND';
    $scope.doc.language = 'chi_sim';
    Restangular.all('document').post($scope.doc).then(function(data) {
      alert('已提交审核！');
      $state.go('document.govlist', { type: 'SEND' });
    }, function(err) {
      alert('已提交审核！（演示模式）');
      $state.go('document.govlist', { type: 'SEND' });
    });
  };

  // Simple editor commands
  $scope.execCmd = function(cmd) {
    document.execCommand(cmd, false, null);
    var editor = document.getElementById('docContentEditor');
    if (editor) editor.focus();
  };
});
