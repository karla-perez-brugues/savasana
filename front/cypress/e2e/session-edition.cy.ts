describe('Session Edition', () => {
  it('should edit a session', () => {
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
      [
        {
          id: 1,
          name: 'Pilates',
          description: 'Reformer pilates',
          date: new Date('2025-02-27'),
          teacher_id: 1,
          users: [],
        }
      ]).as('session');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Pilates',
        description: 'Reformer pilates',
        date: new Date('2025-02-27'),
        teacher_id: 1,
        users: [],
      }
    );

    cy.intercept('GET', '/api/teacher', [
      {
        id: 1,
        lastName: "Teacher",
        firstName: "Yoga",
        createdAt: new Date('2025-02-27'),
        updatedAt: new Date('2025-02-27'),
      }
    ]);

    cy.intercept('PUT', '/api/session/1', {
      body: {
        name: 'Pilates Update',
        description: 'Reformer pilates for beginners',
        date: new Date('2025-02-28'),
        teacher_id: 1,
        users: [],
      }
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          id: 1,
          name: 'Pilates Update',
          description: 'Reformer pilates',
          date: new Date('2025-02-28'),
          teacher_id: 1,
          users: [],
        }
      ]).as('session');

    cy.get('input[formControlName=email]').type("karla@mail.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
    cy.url().should('include', '/sessions');

    cy.get('span').contains('Edit').click();
    cy.url().should('include', 'update');
    cy.title('Update session');

    cy.get('input[formControlName=name]').type(' Update');
    cy.get('input[formControlName=date]').type('2025-02-28');
    cy.get('button').contains('Save').click();

    cy.url().should('include', '/sessions');
  });
});
