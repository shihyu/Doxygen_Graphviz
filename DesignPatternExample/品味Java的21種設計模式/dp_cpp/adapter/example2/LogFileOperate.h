#pragma once

#include "LogFileOperateApi.h"
#include "LogModel.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace adapter
			{
				namespace example2
				{


					///
					/// <summary> * ʵ�ֶ���־�ļ��Ĳ��� </summary>
					/// 
					class LogFileOperate : public LogFileOperateApi
					{
					///	
					///	 <summary> * ��־�ļ���·�����ļ����ƣ�Ĭ���ǵ�ǰclasspath�µ�AdapterLog.log </summary>
					///	 
					private:
						std::string logFilePathName;
					///	
					///	 <summary> * ���췽���������ļ���·�������� </summary>
					///	 * <param name="logFilePathName"> �ļ���·�������� </param>
					///	 
					public:
						LogFileOperate(std::string logFilePathName);
						virtual std::vector<LogModel*> readLogFile();

						virtual void writeLogFile(std::vector<LogModel*> &list);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								logFilePathName = "AdapterLog.log";

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}