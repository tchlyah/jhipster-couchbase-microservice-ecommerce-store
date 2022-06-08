import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Customer e2e test', () => {
  const customerPageUrl = '/customer';
  const customerPageUrlPattern = new RegExp('/customer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/customers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/customers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/customers/*').as('deleteEntityRequest');
  });

  it('should load Customers', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('customer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Customer').should('exist');
    cy.url().should('match', customerPageUrlPattern);
  });

  it('should load details Customer page', function () {
    cy.visit(customerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('customer');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', customerPageUrlPattern);
  });

  it('should load create Customer page', () => {
    cy.visit(customerPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Customer');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', customerPageUrlPattern);
  });

  it('should load edit Customer page', function () {
    cy.visit(customerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Customer');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', customerPageUrlPattern);
  });

  it.skip('should create an instance of Customer', () => {
    cy.visit(customerPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Customer');

    cy.get(`[data-cy="firstName"]`).type('Fredy').should('have.value', 'Fredy');

    cy.get(`[data-cy="lastName"]`).type('Schoen').should('have.value', 'Schoen');

    cy.get(`[data-cy="gender"]`).select('FEMALE');

    cy.get(`[data-cy="email"]`).type('lfv6@)hl3.Fbpp').should('have.value', 'lfv6@)hl3.Fbpp');

    cy.get(`[data-cy="phone"]`).type('354-363-8353 x15600').should('have.value', '354-363-8353 x15600');

    cy.get(`[data-cy="addressLine1"]`).type('Devolved').should('have.value', 'Devolved');

    cy.get(`[data-cy="addressLine2"]`).type('transmit Maine').should('have.value', 'transmit Maine');

    cy.get(`[data-cy="city"]`).type('North Seanberg').should('have.value', 'North Seanberg');

    cy.get(`[data-cy="country"]`).type('Togo').should('have.value', 'Togo');

    cy.setFieldSelectToLastOfEntity('user');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', customerPageUrlPattern);
  });

  it.skip('should delete last instance of Customer', function () {
    cy.intercept('GET', '/api/customers/*').as('dialogDeleteRequest');
    cy.visit(customerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('customer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', customerPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
