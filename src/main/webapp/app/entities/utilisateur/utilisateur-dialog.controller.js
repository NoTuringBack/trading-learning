(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('UtilisateurDialogController', UtilisateurDialogController);

    UtilisateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Utilisateur', 'User', 'Contenu', 'Prestation'];

    function UtilisateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Utilisateur, User, Contenu, Prestation) {
        var vm = this;

        vm.utilisateur = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.contenus = Contenu.query();
        vm.prestations = Prestation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.utilisateur.id !== null) {
                Utilisateur.update(vm.utilisateur, onSaveSuccess, onSaveError);
            } else {
                Utilisateur.save(vm.utilisateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tradinglearningApp:utilisateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
