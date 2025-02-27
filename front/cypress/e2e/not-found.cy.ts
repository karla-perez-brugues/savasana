describe('Not found', () => {
  it('Should show page not found', () => {
    cy.visit('/fake-url');

    cy.title('Page not found !');
  })
})
