#pragma once

#include "LogModel.h"

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
					/// <summary> * ���������־��Ӧ�ýӿڣ�Ϊ��ʾ���ļ򵥣�
					/// * ֻ�Ǽ򵥵Ķ�������ɾ�Ĳ�ķ��� </summary>
					/// 
					class LogDbOperateApi
					{
					///	
					///	 <summary> * ������־ </summary>
					///	 * <param name="lm"> ��Ҫ��������־���� </param>
					///	 
					public:
						virtual void createLog(LogModel*) = 0;
					///	
					///	 <summary> * �޸���־ </summary>
					///	 * <param name="lm"> ��Ҫ�޸ĵ���־���� </param>
					///	 
						virtual void updateLog(LogModel*) = 0;
					///	
					///	 <summary> * ɾ����־ </summary>
					///	 * <param name="lm"> ��Ҫɾ������־���� </param>
					///	 
						virtual void removeLog(LogModel*) = 0;
					///	
					///	 <summary> * ��ȡ���е���־ </summary>
					///	 * <returns> ���е���־���� </returns>
					///	 
						virtual std::vector<LogModel*> getAllLog() = 0;
					};

				}
			}
		}
	}
}
