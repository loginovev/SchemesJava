package ru.loginov.security.service;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.loginov.security.dto.LogColumnOptionsDTO;
import ru.loginov.security.dto.LogDTO;
import ru.loginov.security.dto.LogResponseDTO;
import ru.loginov.security.dto.UserDTO;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;
import ru.loginov.utils.sort_filter.SortFilterUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Logging service
 */
@Service
public class LogServiceImpl implements LogService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * The method gets params for columns of a lazy mode table
     * @return (LogColumnOptionsDTO)
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Logging,'Try to get options of column')")
    public LogColumnOptionsDTO getColumnOptions() {

        String[] messageType = new String[Level.values().length];
        int countLevel = 0;
        for (Level l:Level.values()) {
            messageType[countLevel++] = l.toString();
        }
        Arrays.sort(messageType);

        List<UserDTO>  userDTOS = userService.getAllUsersWithoutPartition();
        String[] username = new String[userDTOS.size()];
        for(int i=0;i<userDTOS.size();i++){
            username[i]=userDTOS.get(i).getUsername();
        }
        Arrays.sort(username);

        String[] event = new String[LoggerEvents.values().length];
        int countEvent = 0;
        for (LoggerEvents l:LoggerEvents.values()) {
            event[countEvent++] = l.toString();
        }
        Arrays.sort(event);

        String[] structure = new String[LoggerAppStructure.values().length];
        int countStructure = 0;
        for (LoggerAppStructure l:LoggerAppStructure.values()) {
            structure[countStructure++] = l.toString();
        }
        Arrays.sort(structure);

        return new LogColumnOptionsDTO(messageType,username,event,structure);
    }

    /**
     * The method gets records from log for a lazy mode table
     * @param first         (int)               - the first record (from 0)
     * @param rows          (int)               - amount of records
     * @param sortFields    ({@literal List<SortField>})   - sorting fields
     * @param filters       ({@literal List<FilterField>}) - filtering fields
     * @return              (LogResponseDTO)    - records and total number of records in log file
     * @throws IOException
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Logging,'Try to get records')")
    public LogResponseDTO getLog(int first, int rows, List<SortField> sortFields, List<FilterField> filters) throws IOException {

        //Check the columns
        SortFilterUtils.checkSortFields(new String[]{"date","messageType","username","event","structure"},sortFields);
        SortFilterUtils.checkFilterFields(new String[]{"date","messageType","username","event","structure"},filters);

        if (filters!=null & filters.size()>0){
            List<FilterField> filtersNew = new ArrayList<>();
            filtersNew.addAll(filters);
            filters.clear();

            for (FilterField filterField:filtersNew) {
                filters.add(new FilterField(filterField.getFieldName(),filterField.getMatchMode(),filterField.valueAsCharArray()));
            }
        }

        //Filename
        String fileName = "./test.log";

        LogDTO[] response = new LogDTO[0];

        List<LogDTO> sortedArray = new ArrayList<>();

        try(SeekableByteChannel seekableByteChannel = Files.newByteChannel(Paths.get(fileName))){

            int count;
            ByteBuffer buffer = ByteBuffer.allocate(10240);

            char currentChar;
            char semicolon = ";".toCharArray()[0];
            char nToken = "\n".toCharArray()[0];//10
            char rToken = "\r".toCharArray()[0];//13

            int maxFieldLength = 2048;

            char[][] fields = new char[9][maxFieldLength];
            int[] fieldsLength = new int[9];

            int countField = 0;

            do {
                count = seekableByteChannel.read(buffer);

                if (count!=-1) {
                    buffer.rewind();

                    for (int bufferCount = 0; bufferCount < count; bufferCount++) {
                        currentChar = (char) buffer.get(bufferCount);

                        if (currentChar==semicolon || currentChar==rToken || currentChar==nToken){

                            if (currentChar==semicolon){
                                countField++;
                            }

                            if(currentChar==nToken){

                                boolean suitable = true;

                                if (filters!=null && filters.size()!=0){
                                    countField = 0;
                                    for (FilterField filterField:filters) {

                                        switch (filterField.getFieldName()){
                                            case "date":{
                                                countField = 0;
                                                break;
                                            }
                                            case "messageType":{
                                                countField = 1;
                                                break;
                                            }

                                            case "username":{
                                                countField = 4;
                                                break;
                                            }
                                            case "event":{
                                                countField = 5;
                                                break;
                                            }
                                            case "structure":{
                                                countField = 6;
                                                break;
                                            }
                                            case "data":{
                                                countField = 7;
                                                break;
                                            }
                                        }

                                        suitable = SortFilterUtils.suitableFilter(
                                                filterField.getMatchMode(),
                                                fields[countField],
                                                fieldsLength[countField],
                                                filterField.valueAsCharArray()
                                        );

                                        if (!suitable){
                                            break;
                                        }
                                    }
                                }

                                if (suitable){
                                    sortedArray.add(new LogDTO(fields,fieldsLength));
                                }

                                countField = 0;
                                for (int fieldsLengthCount=0;fieldsLengthCount<9;fieldsLengthCount++){
                                    fieldsLength[fieldsLengthCount] = 0;
                                }
                            }
                        }else{
                            fields[countField][fieldsLength[countField]++] = currentChar;
                        }
                    }
                }
            }while (count!=-1);

            if (sortedArray.size()>=first){
                response = new LogDTO[Math.min(rows,sortedArray.size()-first)];

                if (sortFields!=null && !sortFields.isEmpty()){
                    sortedArray.sort(SortFilterUtils.getSortComparator(sortFields));
                }

                for (int i=first;i<first+response.length;i++){
                    response[i-first] = sortedArray.get(i);
                }
            }

        }

        return new LogResponseDTO(response,sortedArray.size());
    }
}