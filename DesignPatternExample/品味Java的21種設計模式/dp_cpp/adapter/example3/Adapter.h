#pragma once

#include "LogDbOperateApi.h"
#include "LogFileOperateApi.h"
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
				namespace example3
				{


					///
					/// <summary> * ���������󣬰Ѽ�¼��־���ļ��Ĺ�������ɵڶ�����Ҫ����ɾ�Ĳ�Ĺ��� </summary>
					/// 
					class Adapter : public LogDbOperateApi
					{
					///	
					///	 <summary> * ������Ҫ������Ľӿڶ��� </summary>
					///	 
					private:
						LogFileOperateApi *adaptee;
					///	
					///	 <summary> * ���췽����������Ҫ������Ķ��� </summary>
					///	 * <param name="adaptee"> ��Ҫ������Ķ��� </param>
					///	 
					public:
						Adapter(LogFileOperateApi *adaptee);

						virtual void createLog(LogModel *lm);

						virtual std::vector<LogModel*> getAllLog();

						virtual void removeLog(LogModel *lm);

						virtual void updateLog(LogModel *lm);
					};

				}
			}
		}
	}
}