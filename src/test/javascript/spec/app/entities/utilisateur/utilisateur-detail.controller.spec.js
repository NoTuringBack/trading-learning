'use strict';

describe('Controller Tests', function() {

    describe('Utilisateur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUtilisateur, MockUser, MockContenu, MockPrestation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            MockUser = jasmine.createSpy('MockUser');
            MockContenu = jasmine.createSpy('MockContenu');
            MockPrestation = jasmine.createSpy('MockPrestation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Utilisateur': MockUtilisateur,
                'User': MockUser,
                'Contenu': MockContenu,
                'Prestation': MockPrestation
            };
            createController = function() {
                $injector.get('$controller')("UtilisateurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tradinglearningApp:utilisateurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
