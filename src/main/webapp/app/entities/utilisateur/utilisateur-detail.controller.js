(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('UtilisateurDetailController', UtilisateurDetailController);

    UtilisateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Utilisateur', 'User', 'Contenu', 'Prestation'];

    function UtilisateurDetailController($scope, $rootScope, $stateParams, previousState, entity, Utilisateur, User, Contenu, Prestation) {
        var vm = this;

        vm.utilisateur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tradinglearningApp:utilisateurUpdate', function(event, result) {
            vm.utilisateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
