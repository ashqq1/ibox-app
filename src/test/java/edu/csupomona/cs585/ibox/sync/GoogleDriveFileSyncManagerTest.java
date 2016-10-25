package edu.csupomona.cs585.ibox.sync;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Delete;
import com.google.api.services.drive.Drive.Files.Insert;
import com.google.api.services.drive.Drive.Files.Update;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;


public class GoogleDriveFileSyncManagerTest{
        
        Drive MockDriveService;
        GoogleDriveFileSyncManager gdfsm_test;
        
        private void setup(){
                MockDriveService = Mockito.mock(Drive.class);                
                gdfsm_test = new GoogleDriveFileSyncManager(MockDriveService);
        }
                
        @Test
        public void testAddFile() throws IOException {          
                setup();
                
                File localFile = Mockito.mock(File.class);
                
                Files testDir = Mockito.mock(Files.class);
                when(MockDriveService.files()).thenReturn(testDir);
                
                Insert insertTest = Mockito.mock(Insert.class);
                when(testDir.insert(isA(com.google.api.services.drive.model.File.class), 
                                isA(AbstractInputStreamContent.class))).thenReturn(insertTest);
                
                com.google.api.services.drive.model.File finalFile = 
                                new com.google.api.services.drive.model.File();
                finalFile.setId("Test ID");
                when(insertTest.execute()).thenReturn(finalFile);
                finalFile.getId();              
                
                gdfsm_test.addFile(localFile);
                verify(MockDriveService).files();                                                            
        }


        @Test(expected = Exception.class)  
        public void testExceptionUpdateFile() throws IOException{
                setup();
                
                File localFile = Mockito.mock(File.class);
                when(localFile.getName()).thenReturn(null);
                
                gdfsm_test.updateFile(localFile);
        }
        
        @Test
        public void testUpdateFile() throws IOException{                
                setup();
                                
                File localFile = Mockito.mock(File.class);
                when(localFile.getName()).thenReturn("test.txt");
                
                
                Files testDir = mock(Files.class);
                when(MockDriveService.files()).thenReturn(testDir);
                
                com.google.api.services.drive.Drive.Files.List mockList = 
                                mock(com.google.api.services.drive.Drive.Files.List.class);
                when(testDir.list()).thenReturn(mockList);
                
                com.google.api.services.drive.model.File finalFile = 
                                new com.google.api.services.drive.model.File();
                finalFile.setId("Test ID");
                finalFile.setTitle("test.txt");
                
                com.google.api.services.drive.model.FileList files = 
                                new com.google.api.services.drive.model.FileList();
                List<com.google.api.services.drive.model.File> items = 
                        new ArrayList<com.google.api.services.drive.model.File>();
                items.add(finalFile);
                files.setItems(items);
                
                when(mockList.execute()).thenReturn(files);
                
                String fileId = gdfsm_test.getFileId("test.txt");
                                        
                Update updateTest = Mockito.mock(Update.class);
                when(testDir.update(eq(fileId), isA(com.google.api.services.drive.model.File.class), 
                                isA(AbstractInputStreamContent.class)))
                        .thenReturn(updateTest);
                
                com.google.api.services.drive.model.File finalFile2 = 
                                new com.google.api.services.drive.model.File();
                when(updateTest.execute()).thenReturn(finalFile2);
                
                gdfsm_test.updateFile(localFile);
                verify(MockDriveService, times(3)).files();                                                          
        }

        
        @Test(expected = Exception.class)  
        public void testExceptionDeleteFile() throws IOException{
                Drive MockDriveService = Mockito.mock(Drive.class);          
                GoogleDriveFileSyncManager gdfsm_test = new GoogleDriveFileSyncManager(MockDriveService);
                
                File localFile = Mockito.mock(File.class);
                when(localFile.getName()).thenReturn(null);
                
                gdfsm_test.deleteFile(localFile);
        }
        
        @Test
        public void testGetFileId() throws IOException{
                setup();
                
                Files testDir = mock(Files.class);
                when(MockDriveService.files()).thenReturn(testDir);
                
                com.google.api.services.drive.Drive.Files.List mockList = 
                                mock(com.google.api.services.drive.Drive.Files.List.class);
                when(testDir.list()).thenReturn(mockList);
                
                com.google.api.services.drive.model.File finalFile = 
                                new com.google.api.services.drive.model.File();
                finalFile.setId("Test ID");
                finalFile.setTitle("test.txt");
                
                com.google.api.services.drive.model.FileList files = 
                                new com.google.api.services.drive.model.FileList();
                List<com.google.api.services.drive.model.File> items = 
                        new ArrayList<com.google.api.services.drive.model.File>();
                items.add(finalFile);
                files.setItems(items);
                
                when(mockList.execute()).thenReturn(files);
                                                
                String getId =gdfsm_test.getFileId("test.txt");
                Assert.assertEquals("Test ID", getId);  
        }
        
        
        
        
        @Test
        public void testDeleteFile() throws IOException{
                setup();
                
                Files testDir = mock(Files.class);
                when(MockDriveService.files()).thenReturn(testDir);
                
                com.google.api.services.drive.Drive.Files.List mockList = 
                                mock(com.google.api.services.drive.Drive.Files.List.class);
                when(testDir.list()).thenReturn(mockList);
                
                com.google.api.services.drive.model.File finalFile = 
                                new com.google.api.services.drive.model.File();
                finalFile.setId("Test ID");
                finalFile.setTitle("test.txt");
                
                com.google.api.services.drive.model.FileList files = 
                                new com.google.api.services.drive.model.FileList();
                List<com.google.api.services.drive.model.File> items = 
                        new ArrayList<com.google.api.services.drive.model.File>();
                items.add(finalFile);
                files.setItems(items);
                
                when(mockList.execute()).thenReturn(files);
                
                String fileId = gdfsm_test.getFileId("test.txt");
                                
                File localFile = Mockito.mock(File.class);
                when(localFile.getName()).thenReturn("test.txt");
                
                Delete deleteTest = Mockito.mock(Delete.class);
                when(testDir.delete(eq(fileId)))
                        .thenReturn(deleteTest);                
                                
                gdfsm_test.deleteFile(localFile);
                verify(MockDriveService, times(3)).files();                                                          
        }
        
        
}
