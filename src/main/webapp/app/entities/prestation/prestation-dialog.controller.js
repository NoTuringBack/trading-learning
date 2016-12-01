(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('PrestationDialogController', PrestationDialogController);

    PrestationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Prestation', 'Utilisateur'];

    function PrestationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Prestation, Utilisateur) {
        var vm = this;

        vm.prestation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.utilisateurs = Utilisateur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.prestation.id !== null) {
                Prestation.update(vm.prestation, onSaveSuccess, onSaveError);
            } else {
                Prestation.save(vm.prestation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tradinglearningApp:prestationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDebutLivraison = false;
        vm.datePickerOpenStatus.dateFinLivraison = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
