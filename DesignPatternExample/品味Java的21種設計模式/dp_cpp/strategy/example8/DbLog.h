#pragma once

#include "LogStrategyTemplate.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace strategy
			{
				namespace example8
				{
					///
					/// <summary> * ����־��¼�����ݿ� </summary>
					/// 
					class DbLog : public LogStrategyTemplate
					{
					public:
						virtual void doLog(std::string msg);
					};

				}
			}
		}
	}
}