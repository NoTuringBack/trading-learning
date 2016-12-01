(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prestation', {
            parent: 'entity',
            url: '/prestation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tradinglearningApp.prestation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prestation/prestations.html',
                    controller: 'PrestationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prestation');
                    $translatePartialLoader.addPart('categorie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('prestation-detail', {
            parent: 'entity',
            url: '/prestation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tradinglearningApp.prestation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prestation/prestation-detail.html',
                    controller: 'PrestationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prestation');
                    $translatePartialLoader.addPart('categorie');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Prestation', function($stateParams, Prestation) {
                    return Prestation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prestation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prestation-detail.edit', {
            parent: 'prestation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prestation/prestation-dialog.html',
                    controller: 'PrestationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prestation', function(Prestation) {
                            return Prestation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prestation.new', {
            parent: 'prestation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prestation/prestation-dialog.html',
                    controller: 'PrestationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateDebutLivraison: null,
                                dateFinLivraison: null,
                                categorie: null,
                                titre: null,
                                description: null,
                                nbBeneficiairesMax: null,
                                prix: null,
                                lieu: null,
                                demande: null,
                                effectue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prestation', null, { reload: 'prestation' });
                }, function() {
                    $state.go('prestation');
                });
            }]
        })
        .state('prestation.edit', {
            parent: 'prestation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prestation/prestation-dialog.html',
                    controller: 'PrestationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prestation', function(Prestation) {
                            return Prestation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prestation', null, { reload: 'prestation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prestation.delete', {
            parent: 'prestation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prestation/prestation-delete-dialog.html',
                    controller: 'PrestationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Prestation', function(Prestation) {
                            return Prestation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prestation', null, { reload: 'prestation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
