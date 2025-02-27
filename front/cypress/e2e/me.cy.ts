describe('Me spec', () => {
  it('should display current user information', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        email: "karla@mail.com",
        lastName: "Pérez",
        firstName: "Karla",
        admin: false,
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

    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      {
        id: 1,
        email: "karla@mail.com",
        lastName: "Pérez",
        firstName: "Karla",
        admin: false,
        createdAt: new Date('2025-02-27'),
        updatedAt: new Date('2025-02-27')
      }
    );

    cy.get('input[formControlName=email]').type("karla@mail.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);

    cy.get('span').contains('Account').click();

    cy.title('User information');
  });

  it('click on delete button', () => {
    cy.intercept('DELETE', '/api/user/1', {});

    cy.get('span').contains('Detail').click();

    cy.url().should('include', '/');
  })
})
