(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('ContenuController', ContenuController);

    ContenuController.$inject = ['$scope', '$state', 'DataUtils', 'Contenu', 'ContenuSearch'];

    function ContenuController ($scope, $state, DataUtils, Contenu, ContenuSearch) {
        var vm = this;

        vm.contenus = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Contenu.query(function(result) {
                vm.contenus = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ContenuSearch.query({query: vm.searchQuery}, function(result) {
                vm.contenus = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
