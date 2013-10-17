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
				namespace example8
				{
					///
					/// <summary> * ʵ����־���Եĳ���ģ�壬ʵ�ָ���Ϣ���ʱ�� </summary>
					/// 
					class LogStrategyTemplate : public LogStrategy
					{

					public:
						void log(std::string msg);
					///	
					///	 <summary> * ����ִ����־��¼��������ȥ����ʵ�� </summary>
					///	 * <param name="msg"> ���¼����־��Ϣ </param>
					///	 
					protected:
						virtual void doLog(std::string msg) = 0;
					};
				}
			}
		}
	}
}