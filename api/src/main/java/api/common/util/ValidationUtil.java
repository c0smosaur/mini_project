package api.common.util;

import api.common.error.ReservationErrorCode;
import api.common.exception.ResultException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidationUtil {

    // 날짜 유효성 검증
    public void validateStartAndEndDate(LocalDate startDate,
                                        LocalDate endDate){
        // startDate가 endDate보다 전 (O)
        if (startDate.isBefore(endDate)){
            LocalDate currentDate = LocalDate.now();
            // 오늘 날짜가 startDate보다 뒤 (X)
            if(currentDate.isAfter(startDate)){
                throw new ResultException(ReservationErrorCode.WRONG_DATE);
            }
        }
        // startDate가 endDate보다 나중이거나 같음 (X)
        else throw new ResultException(ReservationErrorCode.WRONG_DATE);
    }
}
