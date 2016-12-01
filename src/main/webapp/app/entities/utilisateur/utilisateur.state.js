(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('utilisateur', {
            parent: 'entity',
            url: '/utilisateur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tradinglearningApp.utilisateur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/utilisateur/utilisateurs.html',
                    controller: 'UtilisateurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('utilisateur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('utilisateur-detail', {
            parent: 'entity',
            url: '/utilisateur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tradinglearningApp.utilisateur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/utilisateur/utilisateur-detail.html',
                    controller: 'UtilisateurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('utilisateur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Utilisateur', function($stateParams, Utilisateur) {
                    return Utilisateur.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'utilisateur',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('utilisateur-detail.edit', {
            parent: 'utilisateur-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/utilisateur/utilisateur-dialog.html',
                    controller: 'UtilisateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Utilisateur', function(Utilisateur) {
                            return Utilisateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('utilisateur.new', {
            parent: 'utilisateur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/utilisateur/utilisateur-dialog.html',
                    controller: 'UtilisateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                credits: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('utilisateur', null, { reload: 'utilisateur' });
                }, function() {
                    $state.go('utilisateur');
                });
            }]
        })
        .state('utilisateur.edit', {
            parent: 'utilisateur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/utilisateur/utilisateur-dialog.html',
                    controller: 'UtilisateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Utilisateur', function(Utilisateur) {
                            return Utilisateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('utilisateur', null, { reload: 'utilisateur' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('utilisateur.delete', {
            parent: 'utilisateur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/utilisateur/utilisateur-delete-dialog.html',
                    controller: 'UtilisateurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Utilisateur', function(Utilisateur) {
                            return Utilisateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('utilisateur', null, { reload: 'utilisateur' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
