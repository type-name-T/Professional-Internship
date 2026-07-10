'use strict';

angular.module('docs').controller('DocumentGovList', function($scope, $stateParams, Restangular) {
  $scope.documents = [];
  $scope.totalCount = 0;
  $scope.currentPage = 1;
  $scope.pageSize = 15;
  $scope.totalPages = 1;
  $scope.loading = false;
  $scope.searchQuery = '';

  // Filter state
  $scope.filter = {
    classification: $stateParams.type || null,
    secrecy: null,
    status: $stateParams.status || null
  };

  // Classification options
  $scope.classifications = [
    { code: 'RECEIVE', name: '收文' },
    { code: 'SEND', name: '发文' },
    { code: 'MINUTES', name: '会议纪要' },
    { code: 'NOTICE', name: '通知公告' },
    { code: 'REPORT', name: '工作报告' },
    { code: 'INTERNAL', name: '内部文件' }
  ];

  // Secrecy levels
  $scope.secrecyLevels = [
    { value: 'PUBLIC', label: '公开' },
    { value: 'INTERNAL', label: '内部' },
    { value: 'SECRET', label: '秘密' },
    { value: 'CONFIDENTIAL', label: '机密' },
    { value: 'TOP_SECRET', label: '绝密' }
  ];

  // Status list
  $scope.statusList = [
    { value: 'DRAFT', label: '拟稿' },
    { value: 'REVIEWING', label: '审核中' },
    { value: 'APPROVED', label: '已签发' },
    { value: 'ARCHIVED', label: '已归档' }
  ];

  // Set filter
  $scope.setFilter = function(key, value) {
    $scope.filter[key] = value;
    $scope.currentPage = 1;
    $scope.loadDocuments();
  };

  // Label helpers
  $scope.getClassLabel = function(code) {
    var map = { RECEIVE: 'label-primary', SEND: 'label-success', MINUTES: 'label-info',
                NOTICE: 'label-warning', REPORT: 'label-default', INTERNAL: 'label-primary' };
    return map[code] || 'label-default';
  };
  $scope.getClassName = function(code) {
    var map = { RECEIVE: '收文', SEND: '发文', MINUTES: '会议纪要',
                NOTICE: '通知公告', REPORT: '工作报告', INTERNAL: '内部文件' };
    return map[code] || code || '内部文件';
  };
  $scope.getSecrecyName = function(level) {
    var map = { PUBLIC: '公开', INTERNAL: '内部', SECRET: '秘密', CONFIDENTIAL: '机密', TOP_SECRET: '绝密' };
    return map[level] || map.INTERNAL;
  };
  $scope.getStatusLabel = function(status) {
    var map = { DRAFT: 'label-default', REVIEWING: 'label-warning', APPROVED: 'label-info', ISSUED: 'label-success', ARCHIVED: 'label-primary' };
    return map[status] || 'label-default';
  };
  $scope.getStatusName = function(status) {
    var map = { DRAFT: '拟稿', REVIEWING: '审核中', APPROVED: '已签发', ISSUED: '已分发', ARCHIVED: '已归档' };
    return map[status] || status || '拟稿';
  };

  // Load documents from API
  $scope.loadDocuments = function() {
    $scope.loading = true;
    var params = {
      limit: $scope.pageSize,
      offset: ($scope.currentPage - 1) * $scope.pageSize,
      sort_column: 3,
      asc: false
    };
    if ($scope.searchQuery) params.search = $scope.searchQuery;

    Restangular.one('document/list').get(params).then(function(data) {
      var docs = (data && data.documents) ? data.documents : [];
      $scope.documents = docs;
      $scope.totalCount = data.total || docs.length;
      $scope.totalPages = Math.ceil($scope.totalCount / $scope.pageSize);
      $scope.loading = false;
    }, function() {
      $scope.documents = [];
      $scope.totalCount = 0;
      $scope.loading = false;
    });
  };

  // Client-side filtering for classification/secrecy/status
  function applyFilters(docs) {
    if (!$scope.filter.classification && !$scope.filter.secrecy && !$scope.filter.status) {
      return docs;
    }
    return docs.filter(function(doc) {
      if ($scope.filter.classification && doc.classification !== $scope.filter.classification) return false;
      if ($scope.filter.secrecy && doc.secrecy_level !== $scope.filter.secrecy) return false;
      if ($scope.filter.status && (doc.doc_status || doc.status) !== $scope.filter.status) return false;
      return true;
    });
  }

  // Computed filtered docs
  $scope.$watchCollection('[documents, filter.classification, filter.secrecy, filter.status]', function() {
    $scope.filteredDocs = applyFilters($scope.documents);
  });

  // Search action
  $scope.doSearch = function() {
    $scope.currentPage = 1;
    $scope.loadDocuments();
  };

  // Initial load
  $scope.loadDocuments();
});
