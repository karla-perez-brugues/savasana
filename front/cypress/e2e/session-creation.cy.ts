describe('Session creation', () => {
  it('should create session', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        email: "karla@mail.com",
        lastName: "Pérez",
        firstName: "Karla",
        admin: true,
        createdAt: new Date('2025-02-27'),
        updatedAt: new Date('2025-02-27')
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

    cy.intercept('GET', '/api/teacher', [
      {
        id: 1,
        lastName: "Teacher",
        firstName: "Yoga",
        createdAt: new Date('2025-02-27'),
        updatedAt: new Date('2025-02-27'),
      }
    ]);

    cy.intercept('POST', '/api/session', {});

    cy.get('input[formControlName=email]').type("karla@mail.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
    cy.url().should('include', '/sessions');

    cy.get('span').contains('Create').click();
    cy.url().should('include', '/create');
    cy.title('Create session');

    cy.get('input[formControlName=name]').type('Pilates');
    cy.get('input[formControlName=date]').type('2025-02-27');
    cy.get('mat-select').click(); // click on teacher list
    cy.get('mat-option').first().click(); // click on first teacher
    cy.get('textarea').type('Reformer pilates for beginners');
    cy.get('button').contains('Save').click();

    cy.url().should('include', '/sessions');
  });
})
