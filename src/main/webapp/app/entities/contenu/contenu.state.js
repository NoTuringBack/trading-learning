(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contenu', {
            parent: 'entity',
            url: '/contenu',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tradinglearningApp.contenu.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contenu/contenus.html',
                    controller: 'ContenuController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contenu');
                    $translatePartialLoader.addPart('type');
                    $translatePartialLoader.addPart('categorie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contenu-detail', {
            parent: 'entity',
            url: '/contenu/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tradinglearningApp.contenu.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contenu/contenu-detail.html',
                    controller: 'ContenuDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contenu');
                    $translatePartialLoader.addPart('type');
                    $translatePartialLoader.addPart('categorie');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Contenu', function($stateParams, Contenu) {
                    return Contenu.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'contenu',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('contenu-detail.edit', {
            parent: 'contenu-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contenu/contenu-dialog.html',
                    controller: 'ContenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contenu', function(Contenu) {
                            return Contenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contenu.new', {
            parent: 'contenu',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contenu/contenu-dialog.html',
                    controller: 'ContenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateCreation: null,
                                type: null,
                                categorie: null,
                                titre: null,
                                description: null,
                                donnees: null,
                                donneesContentType: null,
                                prix: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contenu', null, { reload: 'contenu' });
                }, function() {
                    $state.go('contenu');
                });
            }]
        })
        .state('contenu.edit', {
            parent: 'contenu',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contenu/contenu-dialog.html',
                    controller: 'ContenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contenu', function(Contenu) {
                            return Contenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contenu', null, { reload: 'contenu' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contenu.delete', {
            parent: 'contenu',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contenu/contenu-delete-dialog.html',
                    controller: 'ContenuDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contenu', function(Contenu) {
                            return Contenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contenu', null, { reload: 'contenu' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
