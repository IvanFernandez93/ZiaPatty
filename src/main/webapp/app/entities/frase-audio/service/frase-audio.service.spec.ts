import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFraseAudio } from '../frase-audio.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../frase-audio.test-samples';

import { FraseAudioService } from './frase-audio.service';

const requireRestSample: IFraseAudio = {
  ...sampleWithRequiredData,
};

describe('FraseAudio Service', () => {
  let service: FraseAudioService;
  let httpMock: HttpTestingController;
  let expectedResult: IFraseAudio | IFraseAudio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FraseAudioService);
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

    it('should create a FraseAudio', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const fraseAudio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fraseAudio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FraseAudio', () => {
      const fraseAudio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fraseAudio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FraseAudio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FraseAudio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FraseAudio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFraseAudioToCollectionIfMissing', () => {
      it('should add a FraseAudio to an empty array', () => {
        const fraseAudio: IFraseAudio = sampleWithRequiredData;
        expectedResult = service.addFraseAudioToCollectionIfMissing([], fraseAudio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fraseAudio);
      });

      it('should not add a FraseAudio to an array that contains it', () => {
        const fraseAudio: IFraseAudio = sampleWithRequiredData;
        const fraseAudioCollection: IFraseAudio[] = [
          {
            ...fraseAudio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFraseAudioToCollectionIfMissing(fraseAudioCollection, fraseAudio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FraseAudio to an array that doesn't contain it", () => {
        const fraseAudio: IFraseAudio = sampleWithRequiredData;
        const fraseAudioCollection: IFraseAudio[] = [sampleWithPartialData];
        expectedResult = service.addFraseAudioToCollectionIfMissing(fraseAudioCollection, fraseAudio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fraseAudio);
      });

      it('should add only unique FraseAudio to an array', () => {
        const fraseAudioArray: IFraseAudio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fraseAudioCollection: IFraseAudio[] = [sampleWithRequiredData];
        expectedResult = service.addFraseAudioToCollectionIfMissing(fraseAudioCollection, ...fraseAudioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fraseAudio: IFraseAudio = sampleWithRequiredData;
        const fraseAudio2: IFraseAudio = sampleWithPartialData;
        expectedResult = service.addFraseAudioToCollectionIfMissing([], fraseAudio, fraseAudio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fraseAudio);
        expect(expectedResult).toContain(fraseAudio2);
      });

      it('should accept null and undefined values', () => {
        const fraseAudio: IFraseAudio = sampleWithRequiredData;
        expectedResult = service.addFraseAudioToCollectionIfMissing([], null, fraseAudio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fraseAudio);
      });

      it('should return initial array if no FraseAudio is added', () => {
        const fraseAudioCollection: IFraseAudio[] = [sampleWithRequiredData];
        expectedResult = service.addFraseAudioToCollectionIfMissing(fraseAudioCollection, undefined, null);
        expect(expectedResult).toEqual(fraseAudioCollection);
      });
    });

    describe('compareFraseAudio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFraseAudio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFraseAudio(entity1, entity2);
        const compareResult2 = service.compareFraseAudio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFraseAudio(entity1, entity2);
        const compareResult2 = service.compareFraseAudio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFraseAudio(entity1, entity2);
        const compareResult2 = service.compareFraseAudio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
