import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategoriaFrase } from '../categoria-frase.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../categoria-frase.test-samples';

import { CategoriaFraseService } from './categoria-frase.service';

const requireRestSample: ICategoriaFrase = {
  ...sampleWithRequiredData,
};

describe('CategoriaFrase Service', () => {
  let service: CategoriaFraseService;
  let httpMock: HttpTestingController;
  let expectedResult: ICategoriaFrase | ICategoriaFrase[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CategoriaFraseService);
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

    it('should create a CategoriaFrase', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const categoriaFrase = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(categoriaFrase).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CategoriaFrase', () => {
      const categoriaFrase = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(categoriaFrase).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CategoriaFrase', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CategoriaFrase', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CategoriaFrase', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCategoriaFraseToCollectionIfMissing', () => {
      it('should add a CategoriaFrase to an empty array', () => {
        const categoriaFrase: ICategoriaFrase = sampleWithRequiredData;
        expectedResult = service.addCategoriaFraseToCollectionIfMissing([], categoriaFrase);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaFrase);
      });

      it('should not add a CategoriaFrase to an array that contains it', () => {
        const categoriaFrase: ICategoriaFrase = sampleWithRequiredData;
        const categoriaFraseCollection: ICategoriaFrase[] = [
          {
            ...categoriaFrase,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCategoriaFraseToCollectionIfMissing(categoriaFraseCollection, categoriaFrase);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CategoriaFrase to an array that doesn't contain it", () => {
        const categoriaFrase: ICategoriaFrase = sampleWithRequiredData;
        const categoriaFraseCollection: ICategoriaFrase[] = [sampleWithPartialData];
        expectedResult = service.addCategoriaFraseToCollectionIfMissing(categoriaFraseCollection, categoriaFrase);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaFrase);
      });

      it('should add only unique CategoriaFrase to an array', () => {
        const categoriaFraseArray: ICategoriaFrase[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const categoriaFraseCollection: ICategoriaFrase[] = [sampleWithRequiredData];
        expectedResult = service.addCategoriaFraseToCollectionIfMissing(categoriaFraseCollection, ...categoriaFraseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const categoriaFrase: ICategoriaFrase = sampleWithRequiredData;
        const categoriaFrase2: ICategoriaFrase = sampleWithPartialData;
        expectedResult = service.addCategoriaFraseToCollectionIfMissing([], categoriaFrase, categoriaFrase2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaFrase);
        expect(expectedResult).toContain(categoriaFrase2);
      });

      it('should accept null and undefined values', () => {
        const categoriaFrase: ICategoriaFrase = sampleWithRequiredData;
        expectedResult = service.addCategoriaFraseToCollectionIfMissing([], null, categoriaFrase, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaFrase);
      });

      it('should return initial array if no CategoriaFrase is added', () => {
        const categoriaFraseCollection: ICategoriaFrase[] = [sampleWithRequiredData];
        expectedResult = service.addCategoriaFraseToCollectionIfMissing(categoriaFraseCollection, undefined, null);
        expect(expectedResult).toEqual(categoriaFraseCollection);
      });
    });

    describe('compareCategoriaFrase', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCategoriaFrase(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCategoriaFrase(entity1, entity2);
        const compareResult2 = service.compareCategoriaFrase(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCategoriaFrase(entity1, entity2);
        const compareResult2 = service.compareCategoriaFrase(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCategoriaFrase(entity1, entity2);
        const compareResult2 = service.compareCategoriaFrase(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
