#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace factorymethod
			{
				namespace example7
				{
					///
					/// <summary> * �������ļ�����Ľӿ� </summary>
					/// 
					class ExportFileApi
					{
					///	
					///	 <summary> * �������ݳ�Ϊ�ļ� </summary>
					///	 * <param name="data"> ʾ�⣺��Ҫ��������� </param>
					///	 * <returns> �Ƿ񵼳��ɹ� </returns>
					///	 
					public:
						virtual public bool export(std::string) = 0;
					};

				}
			}
		}
	}
}