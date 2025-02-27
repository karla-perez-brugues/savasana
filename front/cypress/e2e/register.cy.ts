describe('Register spec', () => {
  it('should register successfully', () => {
    cy.visit('/register');

    cy.intercept('POST', 'api/auth/register', {
      body: {
        email: 'karla@mail.com',
        firstName: 'Karla',
        lastName: 'Pérez',
        password: 'password',
      }
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/auth/login',
      },
      []).as('login');

    cy.get('input[formControlName="firstName"]').type('Karla');
    cy.get('input[formControlName="lastName"]').type('Pérez');
    cy.get('input[formControlName="email"]').type('karla@mail.com');
    cy.get('input[formControlName="password"]').type(`${"password"}{enter}{enter}`);

    cy.url().should('include', '/login');
  });
})
