#pragma once

#include "DbLog.h"
#include "LogStrategy.h"
#include "FileLog.h"
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
					/// <summary> * ��־��¼�������� </summary>
					/// 
					class LogContext
					{
					///	
					///	 <summary> * ��¼��־�ķ������ṩ���ͻ���ʹ�� </summary>
					///	 * <param name="msg"> ���¼����־��Ϣ </param>
					///	 
					public:
						virtual void log(std::string msg);
					};

				}
			}
		}
	}
}