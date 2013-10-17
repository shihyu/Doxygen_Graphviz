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
				namespace example5
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
						virtual public void createLog(LogModel*) = 0;
					///	
					///	 <summary> * �޸���־ </summary>
					///	 * <param name="lm"> ��Ҫ�޸ĵ���־���� </param>
					///	 
						virtual public void updateLog(LogModel*) = 0;
					///	
					///	 <summary> * ɾ����־ </summary>
					///	 * <param name="lm"> ��Ҫɾ������־���� </param>
					///	 
						virtual public void removeLog(LogModel*) = 0;
					///	
					///	 <summary> * ��ȡ���е���־ </summary>
					///	 * <returns> ���е���־���� </returns>
					///	 
						virtual public std::vector<LogModel*> getAllLog() = 0;
					};

				}
			}
		}
	}
}