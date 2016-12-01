(function () {
    'use strict';

    angular
        .module('tradinglearningApp')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$state', '$rootScope'];

    function LoginService($state, $rootScope) {

        var authenticationError = false;
        var service = {
            login: login
        };

        return service;

        function login(username, password, rememberMe, vmAuthenticationError, Auth) {
            authenticationError = vmAuthenticationError;

            Auth.login({
                username: username,
                password: password,
                rememberMe: rememberMe
            }).then(function () {
                authenticationError = false;
                if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                    $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                    $state.go('home');
                }

                $rootScope.$broadcast('authenticationSuccess');

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is succesful, go to stored previousState and clear previousState
                if (Auth.getPreviousState()) {
                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();
                    $state.go(previousState.name, previousState.params);
                }
            }).catch(function () {
                $rootScope.$broadcast('authenticationError');
            });
        }
    }
})();
