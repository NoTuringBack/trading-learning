(function() {
    'use strict';
    angular
        .module('tradinglearningApp')
        .factory('Utilisateur', Utilisateur);

    Utilisateur.$inject = ['$resource'];

    function Utilisateur ($resource) {
        var resourceUrl =  'api/utilisateurs/:id';
        var resourceCurrentUtilisateurUrl =  'api/utilisateurs/current';

        var service = {
            utilisateur: utilisateur,
            currentUtilisateur: currentUtilisateur
        };

        return service;

        function utilisateur() {
            return $resource(resourceUrl, {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        if (data) {
                            data = angular.fromJson(data);
                        }
                        return data;
                    }
                },
                'update': {method: 'PUT'}
            });
        };

        function currentUtilisateur() {
            return $resource(resourceCurrentUtilisateurUrl, {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',

                    transformResponse: function (data) {
                        if (data) {
                            data = angular.fromJson(data);
                        }
                        return data;
                    }
                }
            });
        }
    }
})();
