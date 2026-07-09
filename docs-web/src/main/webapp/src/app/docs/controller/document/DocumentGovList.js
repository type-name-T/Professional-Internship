'use strict';

/**
 * Government document list controller.
 */
angular.module('docs').controller('DocumentGovList', function($scope, $state, $stateParams, Restangular) {
  $scope.documents = [];
  $scope.totalCount = 0;
  $scope.currentPage = 1;
  $scope.pageSize = 15;
  $scope.searchQuery = '';
  $scope.listType = $stateParams.type || '';
  $scope.filterSecrecy = null;
  $scope.filterStatus = $stateParams.status || null;

  // Classification name mapping
  var classificationNames = {
    'RECEIVE': '收文',
    'SEND': '发文',
    'MINUTES': '会议纪要',
    'NOTICE': '通知公告',
    'REPORT': '工作报告',
    'REQUEST': '请示报告',
    'INTERNAL': '内部文件'
  };

  $scope.getClassificationName = function(code) {
    return classificationNames[code] || code || '内部文件';
  };

  $scope.getClassificationClass = function(code) {
    var map = {
      'RECEIVE': 'label-primary',
      'SEND': 'label-success',
      'MINUTES': 'label-info',
      'NOTICE': 'label-warning',
      'REPORT': 'label-default',
      'REQUEST': 'label-danger',
      'INTERNAL': 'label-primary'
    };
    return map[code] || 'label-default';
  };

  // Secrecy level mapping
  $scope.getSecrecyName = function(level) {
    var map = {
      'PUBLIC': '公开',
      'INTERNAL': '内部',
      'SECRET': '秘密',
      'CONFIDENTIAL': '机密',
      'TOP_SECRET': '绝密'
    };
    return map[level] || map['INTERNAL'];
  };

  // Status mapping
  $scope.getStatusName = function(status) {
    var map = {
      'DRAFT': '拟稿',
      'REVIEWING': '审核中',
      'APPROVED': '已签发',
      'ISSUED': '已分发',
      'ARCHIVED': '已归档'
    };
    return map[status] || status || '拟稿';
  };

  $scope.getStatusClass = function(status) {
    var map = {
      'DRAFT': 'label-default',
      'REVIEWING': 'label-warning',
      'APPROVED': 'label-info',
      'ISSUED': 'label-success',
      'ARCHIVED': 'label-primary'
    };
    return map[status] || 'label-default';
  };

  // Secrecy filter
  $scope.secrecyFilter = function(doc) {
    if (!$scope.filterSecrecy) return true;
    return doc.secrecyLevel === $scope.filterSecrecy;
  };

  // Status filter
  $scope.statusFilter = function(doc) {
    if (!$scope.filterStatus) return true;
    return doc.status === $scope.filterStatus;
  };

  // Load documents
  $scope.loadDocuments = function() {
    var params = {
      limit: $scope.pageSize,
      offset: ($scope.currentPage - 1) * $scope.pageSize,
      sort_column: 3,
      asc: false
    };
    if ($scope.searchQuery) {
      params.search = $scope.searchQuery;
    }

    Restangular.all('document').getList(params).then(function(data) {
      if (data && data.documents) {
        // Add mock government fields for display
        $scope.documents = data.documents.map(function(d, i) {
          d.docNo = d.docNo || getMockDocNo(d, i);
          d.classification = d.classification || $scope.listType || 'INTERNAL';
          d.secrecyLevel = d.secrecyLevel || 'INTERNAL';
          d.status = d.status || 'DRAFT';
          d.fromUnit = d.fromUnit || d.source || '';
          d.handlerDeptName = d.handlerDeptName || d.creator || '';
          return d;
        });
        $scope.totalCount = data.total || $scope.documents.length;
      }
    });
  };

  // Mock document number for display
  function getMockDocNo(doc, index) {
    var prefix = $scope.listType === 'SEND' ? 'XX发' : 'XX收';
    var year = new Date().getFullYear();
    var seq = String(index + 1).padStart(3, '0');
    return prefix + '〔' + year + '〕' + seq + '号';
  }

  // Search
  $scope.search = function($event) {
    if ($event && $event.keyCode !== 13) return;
    $scope.currentPage = 1;
    $scope.loadDocuments();
  };

  // Page change
  $scope.pageChanged = function() {
    $scope.loadDocuments();
  };

  // Initial load
  $scope.loadDocuments();
});
