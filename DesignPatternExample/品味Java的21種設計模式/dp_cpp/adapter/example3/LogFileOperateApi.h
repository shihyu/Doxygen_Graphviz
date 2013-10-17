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
					/// <summary> * ��־�ļ������ӿ� </summary>
					/// 
					class LogFileOperateApi
					{
					///	
					///	 <summary> * ��ȡ��־�ļ������ļ������ȡ�洢����־�б���� </summary>
					///	 * <returns> �洢����־�б���� </returns>
					///	 
					public:
						virtual public std::vector<LogModel*> readLogFile() = 0;
					///	
					///	 <summary> * д��־�ļ�������־�б�д������־�ļ���ȥ </summary>
					///	 * <param name="list"> Ҫд����־�ļ�����־�б� </param>
					///	 
						virtual public void writeLogFile(std::vector<LogModel*>&) = 0;
					};

				}
			}
		}
	}
}