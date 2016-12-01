(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('ContenuDialogController', ContenuDialogController);

    ContenuDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Contenu', 'Utilisateur'];

    function ContenuDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Contenu, Utilisateur) {
        var vm = this;

        vm.contenu = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            if (vm.contenu.id !== null) {
                Contenu.update(vm.contenu, onSaveSuccess, onSaveError);
            } else {
                Contenu.save(vm.contenu, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tradinglearningApp:contenuUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCreation = false;

        vm.setDonnees = function ($file, contenu) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        contenu.donnees = base64Data;
                        contenu.donneesContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
