#pragma once

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
					/// <summary> * DB�洢��־��ʵ�֣�Ϊ�˼򵥣�����Ͳ�ȥ���ʵ�ֺ����ݿ⽻���ˣ�ʾ��һ�� </summary>
					/// 
					class LogDbOperate : public LogDbOperateApi
					{

					public:
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