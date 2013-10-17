#pragma once

#include "LogFileOperateApi.h"
#include "LogDbOperateApi.h"
#include "LogModel.h"
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace adapter
			{
				namespace example4
				{


					///
					/// <summary> * ˫������������ </summary>
					/// 
					class TwoDirectAdapter : public LogDbOperateApi, LogFileOperateApi
					{
					///	
					///	 <summary> * ������Ҫ��������ļ��洢��־�Ľӿڶ��� </summary>
					///	 
					private:
						LogFileOperateApi *fileLog;
					///	
					///	 <summary> * ������Ҫ�������DB�洢��־�Ľӿڶ��� </summary>
					///	 
						LogDbOperateApi *dbLog;
					///	
					///	 <summary> * ���췽����������Ҫ������Ķ��� </summary>
					///	 * <param name="fileLog"> ��Ҫ��������ļ��洢��־�Ľӿڶ��� </param>
					///	 * <param name="dbLog"> ��Ҫ�������DB�洢��־�Ľӿڶ��� </param>
					///	 
					public:
						TwoDirectAdapter(LogFileOperateApi *fileLog, LogDbOperateApi *dbLog);
					/*-----�����ǰ��ļ������ķ�ʽ�����ΪDBʵ�ַ�ʽ�Ľӿ�-----*/	
						virtual void createLog(LogModel *lm);

						virtual std::vector<LogModel*> getAllLog();

						virtual void removeLog(LogModel *lm);

						virtual void updateLog(LogModel *lm);
					/*-----�����ǰ�DB�����ķ�ʽ�����Ϊ�ļ�ʵ�ַ�ʽ�Ľӿ�-----*/
						virtual std::vector<LogModel*> readLogFile();

						virtual void writeLogFile(std::vector<LogModel*> &list);
					};

				}
			}
		}
	}
}