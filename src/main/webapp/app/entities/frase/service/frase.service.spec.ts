import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFrase } from '../frase.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../frase.test-samples';

import { FraseService, RestFrase } from './frase.service';

const requireRestSample: RestFrase = {
  ...sampleWithRequiredData,
  dataCreazione: sampleWithRequiredData.dataCreazione?.toJSON(),
  dataUltimaModifica: sampleWithRequiredData.dataUltimaModifica?.toJSON(),
};

describe('Frase Service', () => {
  let service: FraseService;
  let httpMock: HttpTestingController;
  let expectedResult: IFrase | IFrase[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FraseService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Frase', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const frase = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(frase).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Frase', () => {
      const frase = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(frase).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Frase', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Frase', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Frase', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFraseToCollectionIfMissing', () => {
      it('should add a Frase to an empty array', () => {
        const frase: IFrase = sampleWithRequiredData;
        expectedResult = service.addFraseToCollectionIfMissing([], frase);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frase);
      });

      it('should not add a Frase to an array that contains it', () => {
        const frase: IFrase = sampleWithRequiredData;
        const fraseCollection: IFrase[] = [
          {
            ...frase,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFraseToCollectionIfMissing(fraseCollection, frase);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Frase to an array that doesn't contain it", () => {
        const frase: IFrase = sampleWithRequiredData;
        const fraseCollection: IFrase[] = [sampleWithPartialData];
        expectedResult = service.addFraseToCollectionIfMissing(fraseCollection, frase);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frase);
      });

      it('should add only unique Frase to an array', () => {
        const fraseArray: IFrase[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fraseCollection: IFrase[] = [sampleWithRequiredData];
        expectedResult = service.addFraseToCollectionIfMissing(fraseCollection, ...fraseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const frase: IFrase = sampleWithRequiredData;
        const frase2: IFrase = sampleWithPartialData;
        expectedResult = service.addFraseToCollectionIfMissing([], frase, frase2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frase);
        expect(expectedResult).toContain(frase2);
      });

      it('should accept null and undefined values', () => {
        const frase: IFrase = sampleWithRequiredData;
        expectedResult = service.addFraseToCollectionIfMissing([], null, frase, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frase);
      });

      it('should return initial array if no Frase is added', () => {
        const fraseCollection: IFrase[] = [sampleWithRequiredData];
        expectedResult = service.addFraseToCollectionIfMissing(fraseCollection, undefined, null);
        expect(expectedResult).toEqual(fraseCollection);
      });
    });

    describe('compareFrase', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFrase(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFrase(entity1, entity2);
        const compareResult2 = service.compareFrase(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFrase(entity1, entity2);
        const compareResult2 = service.compareFrase(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFrase(entity1, entity2);
        const compareResult2 = service.compareFrase(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
