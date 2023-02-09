import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILingua } from '../lingua.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../lingua.test-samples';

import { LinguaService, RestLingua } from './lingua.service';

const requireRestSample: RestLingua = {
  ...sampleWithRequiredData,
  dataCreazione: sampleWithRequiredData.dataCreazione?.toJSON(),
  dataUltimaModifica: sampleWithRequiredData.dataUltimaModifica?.toJSON(),
};

describe('Lingua Service', () => {
  let service: LinguaService;
  let httpMock: HttpTestingController;
  let expectedResult: ILingua | ILingua[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LinguaService);
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

    it('should create a Lingua', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const lingua = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(lingua).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Lingua', () => {
      const lingua = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(lingua).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Lingua', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Lingua', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Lingua', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLinguaToCollectionIfMissing', () => {
      it('should add a Lingua to an empty array', () => {
        const lingua: ILingua = sampleWithRequiredData;
        expectedResult = service.addLinguaToCollectionIfMissing([], lingua);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lingua);
      });

      it('should not add a Lingua to an array that contains it', () => {
        const lingua: ILingua = sampleWithRequiredData;
        const linguaCollection: ILingua[] = [
          {
            ...lingua,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLinguaToCollectionIfMissing(linguaCollection, lingua);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Lingua to an array that doesn't contain it", () => {
        const lingua: ILingua = sampleWithRequiredData;
        const linguaCollection: ILingua[] = [sampleWithPartialData];
        expectedResult = service.addLinguaToCollectionIfMissing(linguaCollection, lingua);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lingua);
      });

      it('should add only unique Lingua to an array', () => {
        const linguaArray: ILingua[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const linguaCollection: ILingua[] = [sampleWithRequiredData];
        expectedResult = service.addLinguaToCollectionIfMissing(linguaCollection, ...linguaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lingua: ILingua = sampleWithRequiredData;
        const lingua2: ILingua = sampleWithPartialData;
        expectedResult = service.addLinguaToCollectionIfMissing([], lingua, lingua2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lingua);
        expect(expectedResult).toContain(lingua2);
      });

      it('should accept null and undefined values', () => {
        const lingua: ILingua = sampleWithRequiredData;
        expectedResult = service.addLinguaToCollectionIfMissing([], null, lingua, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lingua);
      });

      it('should return initial array if no Lingua is added', () => {
        const linguaCollection: ILingua[] = [sampleWithRequiredData];
        expectedResult = service.addLinguaToCollectionIfMissing(linguaCollection, undefined, null);
        expect(expectedResult).toEqual(linguaCollection);
      });
    });

    describe('compareLingua', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLingua(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { idLingua: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLingua(entity1, entity2);
        const compareResult2 = service.compareLingua(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { idLingua: 123 };
        const entity2 = { idLingua: 456 };

        const compareResult1 = service.compareLingua(entity1, entity2);
        const compareResult2 = service.compareLingua(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { idLingua: 123 };
        const entity2 = { idLingua: 123 };

        const compareResult1 = service.compareLingua(entity1, entity2);
        const compareResult2 = service.compareLingua(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
