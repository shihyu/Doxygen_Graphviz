#pragma once

#include "LogStrategy.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace strategy
			{
				namespace example6
				{
					///
					/// <summary> * ����־��¼���ļ� </summary>
					/// 
					class FileLog : public LogStrategy
					{
					public:
						virtual void log(std::string msg);
					};

				}
			}
		}
	}
}