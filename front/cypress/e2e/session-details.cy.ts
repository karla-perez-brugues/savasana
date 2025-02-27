describe('Session details with user account', () => {
  it('should show session details', () => {
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

    cy.get('input[formControlName=email]').type("karla@mail.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);

    cy.get('span').contains('Detail').click();

    cy.title('Pilates');
  });

  it('should participate', () => {
    cy.intercept('POST', '/api/session/1/participate/1', {});

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
        users: [1],
      }
    );

    cy.contains('Participate');
    cy.get('button[color=primary]').click(); // click on participate button
  });

  it('should unparticipate', () => {
    cy.intercept('DELETE', '/api/session/1/participate/1', {});

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

    cy.contains('Do not participate');
    cy.get('button[color=warn]').click(); // click on unparticipate button
  });
});

describe('Session details with admin account', () => {
  it('should delete session', () => {
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

    cy.intercept('DELETE', '/api/session/1', {});

    cy.get('input[formControlName=email]').type("karla@mail.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);

    cy.get('span').contains('Detail').click();

    cy.contains('Delete');
    cy.get('button[color=warn]').click(); // click on delete button
    cy.url().should('include', '/sessions');

  });
});
