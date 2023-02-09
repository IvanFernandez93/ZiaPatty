import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAudio } from '../audio.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../audio.test-samples';

import { AudioService, RestAudio } from './audio.service';

const requireRestSample: RestAudio = {
  ...sampleWithRequiredData,
  dataCreazione: sampleWithRequiredData.dataCreazione?.toJSON(),
  dataUltimaModifica: sampleWithRequiredData.dataUltimaModifica?.toJSON(),
};

describe('Audio Service', () => {
  let service: AudioService;
  let httpMock: HttpTestingController;
  let expectedResult: IAudio | IAudio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AudioService);
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

    it('should create a Audio', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const audio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(audio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Audio', () => {
      const audio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(audio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Audio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Audio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Audio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAudioToCollectionIfMissing', () => {
      it('should add a Audio to an empty array', () => {
        const audio: IAudio = sampleWithRequiredData;
        expectedResult = service.addAudioToCollectionIfMissing([], audio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(audio);
      });

      it('should not add a Audio to an array that contains it', () => {
        const audio: IAudio = sampleWithRequiredData;
        const audioCollection: IAudio[] = [
          {
            ...audio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAudioToCollectionIfMissing(audioCollection, audio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Audio to an array that doesn't contain it", () => {
        const audio: IAudio = sampleWithRequiredData;
        const audioCollection: IAudio[] = [sampleWithPartialData];
        expectedResult = service.addAudioToCollectionIfMissing(audioCollection, audio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(audio);
      });

      it('should add only unique Audio to an array', () => {
        const audioArray: IAudio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const audioCollection: IAudio[] = [sampleWithRequiredData];
        expectedResult = service.addAudioToCollectionIfMissing(audioCollection, ...audioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const audio: IAudio = sampleWithRequiredData;
        const audio2: IAudio = sampleWithPartialData;
        expectedResult = service.addAudioToCollectionIfMissing([], audio, audio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(audio);
        expect(expectedResult).toContain(audio2);
      });

      it('should accept null and undefined values', () => {
        const audio: IAudio = sampleWithRequiredData;
        expectedResult = service.addAudioToCollectionIfMissing([], null, audio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(audio);
      });

      it('should return initial array if no Audio is added', () => {
        const audioCollection: IAudio[] = [sampleWithRequiredData];
        expectedResult = service.addAudioToCollectionIfMissing(audioCollection, undefined, null);
        expect(expectedResult).toEqual(audioCollection);
      });
    });

    describe('compareAudio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAudio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { idAudio: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAudio(entity1, entity2);
        const compareResult2 = service.compareAudio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { idAudio: 123 };
        const entity2 = { idAudio: 456 };

        const compareResult1 = service.compareAudio(entity1, entity2);
        const compareResult2 = service.compareAudio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { idAudio: 123 };
        const entity2 = { idAudio: 123 };

        const compareResult1 = service.compareAudio(entity1, entity2);
        const compareResult2 = service.compareAudio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
